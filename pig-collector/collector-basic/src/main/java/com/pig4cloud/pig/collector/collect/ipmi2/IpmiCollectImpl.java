/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.collector.collect.ipmi2;

import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.collector.collect.AbstractCollect;
import com.pig4cloud.pig.collector.collect.common.cache.AbstractConnection;
import com.pig4cloud.pig.collector.collect.common.cache.CacheIdentifier;
import com.pig4cloud.pig.collector.collect.common.cache.GlobalConnectionCache;
import com.pig4cloud.pig.collector.collect.ipmi2.cache.IpmiConnect;
import com.pig4cloud.pig.collector.collect.ipmi2.client.IpmiClient;
import com.pig4cloud.pig.collector.collect.ipmi2.client.IpmiConnection;
import com.pig4cloud.pig.collector.collect.ipmi2.client.IpmiHandlerManager;
import com.pig4cloud.pig.collector.dispatch.DispatchConstants;
import com.pig4cloud.pig.common.core.entity.job.Metrics;
import com.pig4cloud.pig.common.core.entity.job.protocol.IpmiProtocol;
import com.pig4cloud.pig.common.core.entity.message.CollectRep;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  Ipmi collect implementation
 */
@Slf4j
public class IpmiCollectImpl extends AbstractCollect {

    private final GlobalConnectionCache connectionCommonCache = GlobalConnectionCache.getInstance();

    private final IpmiHandlerManager ipmiHandlerManager;

    // 熔断器相关参数
    private final Map<String, CircuitBreakerState> circuitBreakerMap = new ConcurrentHashMap<>();

    private static class CircuitBreakerState {
        int consecutiveFailures = 0;
        long lastFailureTime = 0;
        boolean isOpen = false;
        static final int FAILURE_THRESHOLD = 5; // 连续失败5次后开启熔断 (更宽松)
        static final long RECOVERY_TIMEOUT = 1 * 60 * 1000L; // 1分钟恢复时间 (更快恢复)
    }

    public IpmiCollectImpl() {
        ipmiHandlerManager = new IpmiHandlerManager();
    }


    @Override
    public void preCheck(Metrics metrics) throws IllegalArgumentException {
        if (metrics == null || metrics.getIpmi() == null) {
            throw new IllegalArgumentException("Ipmi collect must has ipmi params");
        }
        IpmiProtocol ipmiProtocol = metrics.getIpmi();
        Assert.hasText(ipmiProtocol.getHost(), "Ipmi Protocol host is required.");
        Assert.hasText(ipmiProtocol.getPort(), "Ipmi Protocol port is required.");
        Assert.hasText(ipmiProtocol.getUsername(), "Ipmi Protocol username is required.");
        Assert.hasText(ipmiProtocol.getPassword(), "Ipmi Protocol password is required.");
    }

    @Override
    public void collect(CollectRep.MetricsData.Builder builder, Metrics metrics) {
        String circuitKey = metrics.getIpmi().getHost() + ":" + metrics.getIpmi().getPort();

        // 检查熔断器状态
        if (isCircuitBreakerOpen(circuitKey)) {
            // 如果是可用性指标(priority=0)，允许强制重试
            if (metrics.getPriority() == 0) {
                log.warn("Circuit breaker is OPEN for {}, but forcing retry for availability metrics", circuitKey);
                // 临时重置熔断器状态以允许这次尝试
                CircuitBreakerState state = circuitBreakerMap.get(circuitKey);
                if (state != null && System.currentTimeMillis() - state.lastFailureTime > 30000) { // 30秒后允许强制重试
                    log.info("Allowing forced retry for {} after 30 seconds", circuitKey);
                    state.isOpen = false;
                    state.consecutiveFailures = Math.max(0, state.consecutiveFailures - 1); // 减少一次失败计数
                }
            } else {
                log.warn("Circuit breaker is OPEN for {}, skipping collection", circuitKey);
                builder.setCode(CollectRep.Code.FAIL);
                builder.setMsg("Circuit breaker is open - too many consecutive failures. Will retry automatically in " +
                             ((CircuitBreakerState.RECOVERY_TIMEOUT - (System.currentTimeMillis() -
                               circuitBreakerMap.get(circuitKey).lastFailureTime)) / 1000) + " seconds");
                return;
            }
        }

        int maxRetries = 2; // 减少重试次数，防止线程池阻塞
        Exception lastException = null;

        for (int retry = 0; retry < maxRetries; retry++) {
            try {
                IpmiConnection connection = getIpmiConnection(metrics.getIpmi());
                connection.getResource(builder, metrics);

                // 成功时重置熔断器
                resetCircuitBreaker(circuitKey);
                return; // 成功则返回

            } catch (Exception e) {
                lastException = e;
                log.warn("IPMI collect attempt {}/{} failed for {}: {}",
                         retry + 1, maxRetries, circuitKey, e.getMessage());

                // 如果是会话相关错误，清除缓存强制重连
                if (isConnectionError(e)) {
                    CacheIdentifier identifier = CacheIdentifier.builder()
                        .ip(metrics.getIpmi().getHost())
                        .port(metrics.getIpmi().getPort())
                        .username(metrics.getIpmi().getUsername())
                        .password(metrics.getIpmi().getPassword())
                        .build();
                    connectionCommonCache.removeCache(identifier);
                }

                if (retry < maxRetries - 1) {
                    try {
                        Thread.sleep(500 * (retry + 1)); // 缩短延迟：500ms, 1000ms
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        // 所有重试失败，更新熔断器状态
        recordFailure(circuitKey);

        log.error("IPMI collect failed after {} retries for {}: {}",
                  maxRetries, circuitKey,
                  lastException != null ? lastException.getMessage() : "Unknown error");
        builder.setCode(CollectRep.Code.FAIL);
        builder.setMsg("IPMI collect failed after " + maxRetries + " retries: " +
                       (lastException != null ? lastException.getMessage() : "Unknown error"));
    }

    private boolean isConnectionError(Exception e) {
        if (e == null) {
            return false;
        }

        // 检查异常类型
        if (e instanceof IOException ||
            e instanceof java.net.SocketTimeoutException ||
            e instanceof java.net.ConnectException) {
            return true;
        }

        // 检查异常消息
        String msg = e.getMessage();
        if (msg != null) {
            msg = msg.toLowerCase();
            return msg.contains("timeout") ||
                   msg.contains("session") ||
                   msg.contains("connection") ||
                   msg.contains("socket") ||
                   msg.contains("unable to establish") ||
                   msg.contains("communication") ||
                   msg.contains("buffer underflow") ||
                   msg.contains("failed to initialize");
        }

        return false;
    }

    private boolean isCircuitBreakerOpen(String circuitKey) {
        CircuitBreakerState state = circuitBreakerMap.get(circuitKey);
        if (state == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();

        // 如果熔断器开启且恢复时间未到
        if (state.isOpen && (currentTime - state.lastFailureTime < CircuitBreakerState.RECOVERY_TIMEOUT)) {
            return true;
        }

        // 恢复时间到了，尝试半开状态
        if (state.isOpen && (currentTime - state.lastFailureTime >= CircuitBreakerState.RECOVERY_TIMEOUT)) {
            state.isOpen = false;
            state.consecutiveFailures = 0;
            log.info("Circuit breaker for {} is now HALF-OPEN, allowing test request", circuitKey);
        }

        return false;
    }

    private void recordFailure(String circuitKey) {
        CircuitBreakerState state = circuitBreakerMap.computeIfAbsent(circuitKey,
                k -> new CircuitBreakerState());

        state.consecutiveFailures++;
        state.lastFailureTime = System.currentTimeMillis();

        if (state.consecutiveFailures >= CircuitBreakerState.FAILURE_THRESHOLD) {
            state.isOpen = true;
            log.warn("Circuit breaker OPENED for {} after {} consecutive failures",
                     circuitKey, state.consecutiveFailures);
        }
    }

    private void resetCircuitBreaker(String circuitKey) {
        CircuitBreakerState state = circuitBreakerMap.get(circuitKey);
        if (state != null && (state.consecutiveFailures > 0 || state.isOpen)) {
            log.info("Circuit breaker RESET for {} (wa  s {} failures, isOpen: {})",
                     circuitKey, state.consecutiveFailures, state.isOpen);
            state.consecutiveFailures = 0;
            state.isOpen = false;
        }
    }

    /**
     * 手动重置所有熔断器 - 用于故障恢复后的快速重置
     */
    public void resetAllCircuitBreakers() {
        log.info("Manually resetting all circuit breakers");
        circuitBreakerMap.forEach((key, state) -> {
            if (state.isOpen || state.consecutiveFailures > 0) {
                log.info("Resetting circuit breaker for {}", key);
                state.consecutiveFailures = 0;
                state.isOpen = false;
                state.lastFailureTime = 0;
            }
        });
    }

    /**
     * 获取熔断器状态信息
     */
    public Map<String, String> getCircuitBreakerStatus() {
        Map<String, String> status = new java.util.HashMap<>();
        circuitBreakerMap.forEach((key, state) -> {
            status.put(key, String.format("failures=%d, isOpen=%s, lastFailure=%s",
                      state.consecutiveFailures, state.isOpen,
                      new java.util.Date(state.lastFailureTime)));
        });
        return status;
    }

    @Override
    public String supportProtocol() {
        return DispatchConstants.PROTOCOL_IPMI;
    }


    private IpmiConnection getIpmiConnection(IpmiProtocol ipmiProtocol) throws Exception {
        CacheIdentifier identifier = CacheIdentifier.builder()
                .ip(ipmiProtocol.getHost())
                .port(ipmiProtocol.getPort())
                .username(ipmiProtocol.getUsername())
                .password(ipmiProtocol.getPassword())
                .build();
        IpmiConnection connection = null;
        Optional<AbstractConnection<?>> cacheOption = connectionCommonCache.getCache(identifier, true);
        if (cacheOption.isPresent()) {
            IpmiConnect ipmiConnect = (IpmiConnect) cacheOption.get();
            connection = ipmiConnect.getConnection();
            if (connection == null || !connection.isActive()) {
                log.info("Cached IPMI connection for {} is inactive, removing from cache", ipmiProtocol.getHost());
                connection = null;
                connectionCommonCache.removeCache(identifier);
            } else {
                log.debug("Reusing cached IPMI connection for {}", ipmiProtocol.getHost());
            }
        }
        if (connection != null) {
            return connection;
        }

        // 创建新连接前进行快速网络可达性检查
        if (!isHostReachable(ipmiProtocol.getHost(), Integer.parseInt(ipmiProtocol.getPort()))) {
            log.warn("Host {}:{} appears unreachable, but attempting connection anyway",
                     ipmiProtocol.getHost(), ipmiProtocol.getPort());
            // 不抛异常，仅记录警告，让IPMI连接自己判断
        }

        log.info("Creating new IPMI connection to {}", ipmiProtocol.getHost());
        IpmiClient ipmiClient = IpmiClient.create(ipmiProtocol);
        connection = ipmiClient.connect();

        // 使用更短的缓存时间：2分钟
        connectionCommonCache.addCache(identifier, new IpmiConnect(connection), 2 * 60 * 1000L);
        return connection;
    }

    private boolean isHostReachable(String host, int port) {
        try (java.net.Socket socket = new java.net.Socket()) {
            long startTime = System.currentTimeMillis();
            socket.connect(new java.net.InetSocketAddress(host, port), 5000); // 5秒连接超时
            long connectTime = System.currentTimeMillis() - startTime;
            log.debug("Host {}:{} is reachable (connect time: {}ms)", host, port, connectTime);
            return true;
        } catch (Exception e) {
            log.warn("Host {}:{} is not reachable: {} ({})", host, port, e.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }

    /**
     * 诊断IPMI连接问题
     */
    public String diagnoseIpmiConnection(String host, String port) {
        StringBuilder diagnosis = new StringBuilder();
        diagnosis.append("IPMI Connection Diagnosis for ").append(host).append(":").append(port).append("\n");

        // 1. 网络可达性检查
        boolean reachable = isHostReachable(host, Integer.parseInt(port));
        diagnosis.append("1. Network reachability: ").append(reachable ? "OK" : "FAILED").append("\n");

        // 2. 熔断器状态
        String circuitKey = host + ":" + port;
        CircuitBreakerState state = circuitBreakerMap.get(circuitKey);
        if (state != null) {
            diagnosis.append("2. Circuit breaker status: ")
                     .append("failures=").append(state.consecutiveFailures)
                     .append(", isOpen=").append(state.isOpen)
                     .append(", lastFailure=").append(new java.util.Date(state.lastFailureTime))
                     .append("\n");
        } else {
            diagnosis.append("2. Circuit breaker status: No previous failures\n");
        }

        // 3. 缓存状态
        CacheIdentifier identifier = CacheIdentifier.builder()
                .ip(host).port(port).username("unknown").password("unknown").build();
        Optional<AbstractConnection<?>> cached = connectionCommonCache.getCache(identifier, false);
        diagnosis.append("3. Connection cache: ").append(cached.isPresent() ? "EXISTS" : "EMPTY").append("\n");

        return diagnosis.toString();
    }
}
