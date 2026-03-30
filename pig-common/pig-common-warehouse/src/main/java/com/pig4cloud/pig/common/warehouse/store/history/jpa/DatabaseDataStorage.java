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

package com.pig4cloud.pig.common.warehouse.store.history.jpa;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.constants.MetricDataConstants;
import com.pig4cloud.pig.common.core.entity.arrow.ArrowCell;
import com.pig4cloud.pig.common.core.entity.arrow.RowWrapper;
import com.pig4cloud.pig.common.core.entity.dto.Value;
import com.pig4cloud.pig.common.core.entity.message.CollectRep;
import com.pig4cloud.pig.common.core.entity.warehouse.History;
import com.pig4cloud.pig.common.core.util.JsonUtil;
import com.pig4cloud.pig.common.core.util.TimePeriodUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.pig.common.warehouse.mapper.HistoryMapper;
import com.pig4cloud.pig.common.warehouse.store.history.AbstractHistoryDataStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * data storage by mysql/h2 - mybatis plus
 */
@Component
@ConditionalOnProperty(prefix = "warehouse.store.jpa", name = "enabled", havingValue = "true")
@Slf4j
public class DatabaseDataStorage extends AbstractHistoryDataStorage {
    private final HistoryMapper historyMapper;
    private final JpaProperties jpaProperties;

    private static final int STRING_MAX_LENGTH = 1024;

    public DatabaseDataStorage(JpaProperties jpaProperties,
                                  HistoryMapper historyMapper) {
        this.jpaProperties = jpaProperties;
        this.serverAvailable = true;
        this.historyMapper = historyMapper;
        expiredDataCleaner();
    }

    public void expiredDataCleaner() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler((thread, throwable) -> {
                    log.error("Jpa metrics store has uncaughtException.");
                    log.error(throwable.getMessage(), throwable);
                })
                .setDaemon(true)
                .setNameFormat("jpa-metrics-cleaner-%d")
                .build();
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(threadFactory);
        scheduledExecutor.scheduleAtFixedRate(() -> {
            log.warn("[jpa-metrics-store]-start running expired data cleaner."
                    + "Please use time series db instead of jpa for better performance");
            String expireTimeStr = jpaProperties.expireTime();
            long expireTime;
            try {
                if (NumberUtils.isParsable(expireTimeStr)) {
                    expireTime = NumberUtils.toLong(expireTimeStr);
                    expireTime = (ZonedDateTime.now().toEpochSecond() + expireTime) * 1000L;
                } else {
                    TemporalAmount temporalAmount = TimePeriodUtil.parseTokenTime(expireTimeStr);
                    ZonedDateTime dateTime = ZonedDateTime.now().minus(temporalAmount);
                    expireTime = dateTime.toEpochSecond() * 1000L;
                }
            } catch (Exception e) {
                log.error("expiredDataCleaner time error: {}. use default expire time to clean: 1h", e.getMessage());
                ZonedDateTime dateTime = ZonedDateTime.now().minus(Duration.ofHours(1));
                expireTime = dateTime.toEpochSecond() * 1000L;
            }
            try {
                int rows = historyMapper.delete(new LambdaQueryWrapper<History>().lt(History::getTime, expireTime));
                log.info("[jpa-metrics-store]-delete {} rows.", rows);
                long total = historyMapper.selectCount(null);
                if (total > jpaProperties.maxHistoryRecordNum()) {
                    rows = historyMapper.deleteOlderHistoriesRecord(jpaProperties.maxHistoryRecordNum() / 2);
                    log.warn("[jpa-metrics-store]-force delete {} rows due too many. Please use time series db instead of jpa for better performance.", rows);
                }
            } catch (Exception e) {
                log.error("expiredDataCleaner database error: {}.", e.getMessage());
                log.error("try to truncate table hzb_history. Please use time series db instead of jpa for better performance.");
                historyMapper.truncateTable();
            }
        }, 5, 30, TimeUnit.SECONDS);
    }

    @Override
    public void saveData(CollectRep.MetricsData metricsData) {
        if (metricsData.getCode() != CollectRep.Code.SUCCESS) {
            return;
        }
        if (metricsData.getValues().isEmpty()) {
            log.info("[warehouse jpa] flush metrics data {} is null, ignore.", metricsData.getId());
            return;
        }
        String monitorType = metricsData.getApp();
        String metrics = metricsData.getMetrics();

        try {
            List<History> allHistoryList = Lists.newArrayList();
            Map<String, String> labels = Maps.newHashMapWithExpectedSize(8);
            RowWrapper rowWrapper = metricsData.readRow();

            while (rowWrapper.hasNextRow()) {
                rowWrapper = rowWrapper.nextRow();
                List<History> singleHistoryList = new ArrayList<>();

                rowWrapper.cellStream().forEach(cell -> singleHistoryList.add(buildHistory(metricsData, cell, monitorType, metrics, labels)));
                singleHistoryList.forEach(history -> history.setInstance(JsonUtil.toJson(labels)));

                allHistoryList.addAll(singleHistoryList);
            }

            allHistoryList.forEach(historyMapper::insert);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private History buildHistory(CollectRep.MetricsData metricsData, ArrowCell cell, String monitorType, String metrics, Map<String, String> labels) {
        History.HistoryBuilder historyBuilder = History.builder()
                .monitorId(metricsData.getId())
                .app(monitorType)
                .metrics(metrics)
                .time(metricsData.getTime())
                .metric(cell.getField().getName());

        final String columnValue = cell.getValue();
        final int fieldType = cell.getMetadataAsInteger(MetricDataConstants.TYPE);
        if (CommonConstants.NULL_VALUE.equals(columnValue)) {
            switch (fieldType) {
                case CommonConstants.TYPE_NUMBER ->
                        historyBuilder.metricType(CommonConstants.TYPE_NUMBER)
                                .dou(null);
                case CommonConstants.TYPE_STRING ->
                        historyBuilder.metricType(CommonConstants.TYPE_STRING)
                                .str(null);
                case CommonConstants.TYPE_TIME -> historyBuilder.metricType(CommonConstants.TYPE_TIME)
                        .int32(null);
                default -> historyBuilder.metricType(CommonConstants.TYPE_NUMBER);
            }
        } else {
            switch (fieldType) {
                case CommonConstants.TYPE_STRING ->
                        historyBuilder.metricType(CommonConstants.TYPE_STRING)
                                .str(formatStrValue(columnValue));
                case CommonConstants.TYPE_TIME -> historyBuilder.metricType(CommonConstants.TYPE_TIME)
                        .int32(Integer.parseInt(columnValue));
                default -> historyBuilder.metricType(CommonConstants.TYPE_NUMBER)
                        .dou(Double.parseDouble(columnValue));
            }

            if (cell.getMetadataAsBoolean(MetricDataConstants.LABEL)) {
                labels.put(cell.getField().getName(), columnValue);
            }
        }

        return historyBuilder.build();
    }

    @Override
    public Map<String, List<Value>> getHistoryMetricData(Long monitorId, String app, String metrics, String metric, String label, String history) {
        Map<String, List<Value>> instanceValuesMap = new HashMap<>(8);
        LambdaQueryWrapper<History> queryWrapper = new LambdaQueryWrapper<History>()
                .eq(History::getMonitorId, monitorId)
                .eq(History::getMetrics, metrics)
                .eq(History::getMetric, metric);

        if (CommonConstants.PROMETHEUS.equals(app)) {
            queryWrapper.likeRight(History::getApp, CommonConstants.PROMETHEUS_APP_PREFIX);
        } else {
            queryWrapper.eq(History::getApp, app);
        }

        if (StringUtils.isNotBlank(label)) {
            queryWrapper.eq(History::getInstance, label);
        }

        if (history != null) {
            try {
                TemporalAmount temporalAmount = TimePeriodUtil.parseTokenTime(history);
                ZonedDateTime dateTime = ZonedDateTime.now().minus(temporalAmount);
                long timeBefore = dateTime.toEpochSecond() * 1000L;
                queryWrapper.ge(History::getTime, timeBefore);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        queryWrapper.orderByDesc(History::getTime);
        List<History> historyList = historyMapper.selectList(queryWrapper);
        for (History dataItem : historyList) {
            String value = "";
            if (dataItem.getMetricType() == CommonConstants.TYPE_NUMBER) {
                if (dataItem.getDou() != null) {
                    value = BigDecimal.valueOf(dataItem.getDou()).setScale(4, RoundingMode.HALF_UP)
                            .stripTrailingZeros().toPlainString();
                }
            } else {
                value = dataItem.getStr();
            }
            String instanceValue = dataItem.getInstance() == null ? "" : dataItem.getInstance();
            List<Value> valueList = instanceValuesMap.computeIfAbsent(instanceValue, k -> new LinkedList<>());
            valueList.add(new Value(value, dataItem.getTime()));
        }
        return instanceValuesMap;
    }

    private String formatStrValue(String value) {
        if (value == null) {
            return "";
        }
        value = value.replace("'", "\\'");
        value = value.replace("\"", "\\\"");
        value = value.replace("*", "-");
        value = String.format("`%s`", value);
        if (value.length() > STRING_MAX_LENGTH) {
            value = value.substring(0, STRING_MAX_LENGTH - 1);
        }
        return value;
    }

    @Override
    public Map<String, List<Value>> getHistoryIntervalMetricData(Long monitorId, String app, String metrics, String metric, String label, String history) {
        return new HashMap<>(8);
    }

    @Override
    public void destroy() throws Exception {
    }
}
