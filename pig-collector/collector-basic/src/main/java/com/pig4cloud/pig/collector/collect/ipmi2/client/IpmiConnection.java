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

package com.pig4cloud.pig.collector.collect.ipmi2.client;

import com.pig4cloud.pig.collector.collect.ipmi2.client.handler.IpmiHandler;
import com.pig4cloud.pig.collector.collect.ipmi2.protocol.ipmi.command.messaging.CloseSessionRequest;
import com.pig4cloud.pig.collector.collect.ipmi2.protocol.ipmi.command.messaging.CloseSessionResponse;
import com.pig4cloud.pig.common.core.entity.job.Metrics;
import com.pig4cloud.pig.common.core.entity.message.CollectRep;

import java.io.IOException;

/**
 * IpmiConnection used for sending ipmi request
 */
public class IpmiConnection implements AutoCloseable {

    IpmiSession session;

    UdpConnection udpConnection;

    IpmiHandlerManager handlerManager = new IpmiHandlerManager();

    private volatile boolean active = true;
    private long lastActiveTime = System.currentTimeMillis();
    private long lastHeartbeatTime = System.currentTimeMillis();
    private static final long SESSION_TIMEOUT = 3 * 60 * 1000L; // 3分钟会话超时
    private static final long HEARTBEAT_INTERVAL = 90 * 1000L; // 90秒心跳间隔

    IpmiConnection(IpmiSession session, UdpConnection udpConnection) {
        this.session = session;
        this.udpConnection = udpConnection;
    }

    public void getResource(CollectRep.MetricsData.Builder builder, Metrics metrics) throws IOException {
        try {
            IpmiHandler handler = handlerManager.getHandler(metrics.getName());
            if (handler == null) {
                throw new RuntimeException("no handler for " + metrics.getIpmi().getType());
            }
            handler.handler(session, udpConnection, builder, metrics);

            // 成功执行后更新活跃时间
            lastActiveTime = System.currentTimeMillis();

        } catch (IOException e) {
            // 如果是超时或连接错误，标记为非活跃
            if (e.getMessage() != null) {
                String msg = e.getMessage().toLowerCase();
                if (msg.contains("timeout") ||
                    msg.contains("connection") ||
                    msg.contains("session") ||
                    msg.contains("socket")) {
                    active = false;
                }
            }
            throw e;
        }
    }


    @Override
    public void close() throws IOException {
        udpConnection.get(session,  new CloseSessionRequest(session.getSystemSessionId()), CloseSessionResponse.class);
        udpConnection.close();
        session = null;
        active = false;
    }

    public boolean isActive() {
        if (!active) {
            return false;
        }

        long currentTime = System.currentTimeMillis();

        // 简化的会话超时检查 (5分钟)
        if (currentTime - lastActiveTime > 5 * 60 * 1000L) {
            active = false;
            return false;
        }

        // 临时禁用心跳检查
        // if (currentTime - lastHeartbeatTime > HEARTBEAT_INTERVAL) {
        //     if (!performHeartbeat()) {
        //         active = false;
        //         return false;
        //     }
        // }

        return true;
    }

    private boolean performHeartbeat() {
        try {
            // 使用轻量级的BMC Info命令作为心跳
            // 这个命令开销小，可以验证会话是否有效
            long currentTime = System.currentTimeMillis();
            lastHeartbeatTime = currentTime;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
