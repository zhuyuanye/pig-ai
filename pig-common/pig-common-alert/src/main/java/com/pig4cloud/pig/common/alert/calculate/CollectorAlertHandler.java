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

package com.pig4cloud.pig.common.alert.calculate;

import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.pig.common.alert.mapper.AlertCollectorMapper;
import com.pig4cloud.pig.common.alert.reduce.AlarmCommonReduce;
import com.pig4cloud.pig.common.alert.util.AlertUtil;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.entity.alerter.SingleAlert;
import com.pig4cloud.pig.common.core.entity.manager.Collector;
import com.pig4cloud.pig.common.core.support.event.SystemConfigChangeEvent;
import com.pig4cloud.pig.common.core.util.ResourceBundleUtil;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * handle collector alarm
 */
@Component
@Slf4j
public class CollectorAlertHandler {

    private static final String KEY_COLLECTOR_NAME = "collectorName";
    private static final String KEY_COLLECTOR_VERSION = "collectorVersion";
    private static final String KEY_COLLECTOR_HOST = "collectorHost";

    private final AlertCollectorMapper alertCollectorMapper;

    private final AlarmCommonReduce alarmCommonReduce;

    private final AlarmCacheManager alarmCacheManager;

    private ResourceBundle bundle;


    public CollectorAlertHandler(AlarmCommonReduce alarmCommonReduce, AlertCollectorMapper alertCollectorMapper,
                                 AlarmCacheManager alarmCacheManager) {
        this.alarmCommonReduce = alarmCommonReduce;
        this.alertCollectorMapper = alertCollectorMapper;
        this.alarmCacheManager = alarmCacheManager;
        this.bundle = ResourceBundleUtil.getBundle("alerter");
    }

    /**
     * handle collector online
     *
     * @param identity collector name
     */
    public void online(final String identity) {
        Collector collector = alertCollectorMapper.selectOne(
                new LambdaQueryWrapper<Collector>().eq(Collector::getName, identity));
        if (collector == null) {
            return;
        }
        Map<String, String> fingerPrints = new HashMap<>(8);
        fingerPrints.put(KEY_COLLECTOR_NAME, collector.getName());
        fingerPrints.put(KEY_COLLECTOR_VERSION, collector.getVersion());
        fingerPrints.put(KEY_COLLECTOR_HOST, collector.getIp());
        String fingerprint = AlertUtil.calculateFingerprint(fingerPrints);
        SingleAlert firingAlert = alarmCacheManager.getFiring(fingerprint);
        if (firingAlert != null) {
            firingAlert.setTriggerTimes(1);
            firingAlert.setEndAt(System.currentTimeMillis());
            firingAlert.setStatus(CommonConstants.ALERT_STATUS_RESOLVED);
            alarmCommonReduce.reduceAndSendAlarm(firingAlert.clone());
        }
    }


    /**
     * handle collector offline
     *
     * @param identity collector name
     */
    public void offline(final String identity) {
        Collector collector = alertCollectorMapper.selectOne(
                new LambdaQueryWrapper<Collector>().eq(Collector::getName, identity));
        if (collector == null) {
            return;
        }
        long currentTimeMill = System.currentTimeMillis();
        Map<String, String> fingerPrints = new HashMap<>(8);
        fingerPrints.put(KEY_COLLECTOR_NAME, collector.getName());
        fingerPrints.put(KEY_COLLECTOR_VERSION, collector.getVersion());
        fingerPrints.put(KEY_COLLECTOR_HOST, collector.getIp());
        String fingerprint = AlertUtil.calculateFingerprint(fingerPrints);
        SingleAlert existingAlert = alarmCacheManager.getFiring(fingerprint);
        if (existingAlert == null) {
            SingleAlert newAlert = SingleAlert.builder()
                    .labels(fingerPrints)
                    .annotations(fingerPrints)
                    .content(this.bundle.getString("alerter.availability.collector.offline"))
                    .status(CommonConstants.ALERT_STATUS_FIRING)
                    .triggerTimes(1)
                    .startAt(currentTimeMill)
                    .activeAt(currentTimeMill)
                    .build();
            alarmCacheManager.putFiring(fingerprint, newAlert);
            alarmCommonReduce.reduceAndSendAlarm(newAlert.clone());
        }

    }


    @EventListener(SystemConfigChangeEvent.class)
    public void onSystemConfigChangeEvent(SystemConfigChangeEvent event) {
        log.info("calculate alarm receive system config change event: {}.", event.getSource());
        this.bundle = ResourceBundleUtil.getBundle("alerter");
    }

}
