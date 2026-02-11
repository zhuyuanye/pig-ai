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

package com.pig4cloud.pig.collector.timer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.collector.dispatch.DispatchConstants;
import com.pig4cloud.pig.collector.dispatch.MetricsTaskDispatch;
import com.pig4cloud.pig.collector.util.CollectUtil;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.entity.job.Configmap;
import com.pig4cloud.pig.common.core.entity.job.Job;
import com.pig4cloud.pig.common.core.entity.job.Metrics;
import com.pig4cloud.pig.common.core.support.SpringContextHolder;
import com.pig4cloud.pig.common.core.timer.Timeout;
import com.pig4cloud.pig.common.core.timer.TimerTask;
import com.pig4cloud.pig.common.core.util.AesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Timer Task implementation
 */
@Slf4j
public class WheelTimerTask implements TimerTask {

    private final Job job;
    private final MetricsTaskDispatch metricsTaskDispatch;
    private static final Gson GSON = new Gson();

    public WheelTimerTask(Job job) {
        this.metricsTaskDispatch = SpringContextHolder.getBean(MetricsTaskDispatch.class);
        this.job = job;
        // The initialization job will monitor the actual parameter value and replace the collection field
        initJobMetrics(job);
    }

    /**
     * Initialize job fill information
     * @param job job
     */
    private void initJobMetrics(Job job) {
        List<Configmap> config = job.getConfigmap();
        Map<String, Configmap> configmap = config.stream()
                .peek(item -> {
                    // decode password
                    if (item.getType() == CommonConstants.PARAM_TYPE_PASSWORD && item.getValue() != null) {
                        String decodeValue = AesUtil.aesDecode(String.valueOf(item.getValue()));
                        if (decodeValue == null) {
                            log.error("Aes Decode value {} error.", item.getValue());
                        }
                        item.setValue(decodeValue);
                    } else if (item.getValue() != null && item.getValue() instanceof String value) {
                        item.setValue(value.trim());
                    }
                })
                .collect(Collectors.toMap(Configmap::getKey, item -> item, (key1, key2) -> key1));
        List<Metrics> metrics = job.getMetrics();
        List<Metrics> metricsTmp = new ArrayList<>(metrics.size());
        for (Metrics metric : metrics) {
            JsonElement jsonElement = GSON.toJsonTree(metric);
            CollectUtil.replaceSmilingPlaceholder(jsonElement, configmap);
            metric = GSON.fromJson(jsonElement, Metrics.class);
            if (job.getApp().equals(DispatchConstants.PROTOCOL_PUSH)) {
                CollectUtil.replaceFieldsForPushStyleMonitor(metric, configmap);
            }
            metricsTmp.add(metric);
        }
        job.setMetrics(metricsTmp);
        job.initIntervals();
    }


    @Override
    public void run(Timeout timeout) throws Exception {
        job.setDispatchTime(System.currentTimeMillis());
        metricsTaskDispatch.dispatchMetricsTask(timeout);
    }

    public Job getJob() {
        return job;
    }
}
