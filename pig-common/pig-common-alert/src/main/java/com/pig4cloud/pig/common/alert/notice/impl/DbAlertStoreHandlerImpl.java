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

package com.pig4cloud.pig.common.alert.notice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.alert.dao.AlertHistoryDao;
import com.pig4cloud.pig.common.alert.dao.GroupAlertDao;
import com.pig4cloud.pig.common.alert.dao.SingleAlertDao;
import com.pig4cloud.pig.common.alert.notice.AlertStoreHandler;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.entity.alerter.AlertHistory;
import com.pig4cloud.pig.common.core.entity.alerter.GroupAlert;
import com.pig4cloud.pig.common.core.entity.alerter.SingleAlert;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Alarm data persistence - landing in the database
 */
@Component
@RequiredArgsConstructor
@Slf4j
final class DbAlertStoreHandlerImpl implements AlertStoreHandler {

    private final GroupAlertDao groupAlertDao;

    private final SingleAlertDao singleAlertDao;
    //历史数据入库DAO
    private final AlertHistoryDao alertHistoryDao;

    @Override
    public GroupAlert store(GroupAlert groupAlert) {
        if (groupAlert == null || groupAlert.getAlerts() == null || groupAlert.getAlerts().isEmpty()) {
            log.error("The Group Alerts is empty, ignore store");
            return groupAlert;
        }
        // Process individual alerts
        Set<String> alertFingerprints = new HashSet<>(8);
        List<SingleAlert> originalAlerts = groupAlert.getAlerts();
        List<SingleAlert> newAlerts = new ArrayList<>();

        for (SingleAlert singleAlert : originalAlerts) {
            synchronized (singleAlert.getFingerprint().intern()) {
                SingleAlert existAlert = singleAlertDao.findByFingerprint(singleAlert.getFingerprint());
                if (existAlert != null) {
                    // Update the existing alert with the ID and creation time from the database
                    singleAlert.setId(existAlert.getId());
                    singleAlert.setGmtCreate(existAlert.getGmtCreate());
                    //添加历史报警数据
                    singleAlert.setFingerprintId(saveAlertHistory(singleAlert));
                    // Status transition logic
                    if (CommonConstants.ALERT_STATUS_FIRING.equals(singleAlert.getStatus())) {
                        // If the alert is firing and the existing alert is not resolved, update the start time and trigger times
                        if (!CommonConstants.ALERT_STATUS_RESOLVED.equals(existAlert.getStatus())) {
                            singleAlert.setStartAt(existAlert.getStartAt());
                            int triggerTimes = Optional.ofNullable(existAlert.getTriggerTimes()).orElse(1)
                                    + Optional.ofNullable(singleAlert.getTriggerTimes()).orElse(1);
                            singleAlert.setTriggerTimes(triggerTimes);
                        }
                    } else if (CommonConstants.ALERT_STATUS_RESOLVED.equals(singleAlert.getStatus())) {
                        // If the alert is resolved, set the end time (if not already set) and copy other fields from the existing alert
                        if (singleAlert.getEndAt() == null) {
                            singleAlert.setEndAt(System.currentTimeMillis());
                        }
                        singleAlert.setStartAt(existAlert.getStartAt());
                        singleAlert.setActiveAt(existAlert.getActiveAt());
                        singleAlert.setTriggerTimes(existAlert.getTriggerTimes());
                        //改变历史报警状态
                        updateAlertsByFingerprintId(singleAlert.getFingerprintId());

                    }
                } else {
                    singleAlert.setFingerprintId(saveAlertHistory(singleAlert));
                }
                SingleAlert savedSingleAlert = singleAlertDao.save(singleAlert);
                newAlerts.add(savedSingleAlert);
                alertFingerprints.add(savedSingleAlert.getFingerprint());
            }
        }
        groupAlert.setAlerts(newAlerts);
        // Find existing alert group
        synchronized (groupAlert.getGroupKey().intern()) {
            GroupAlert existGroupAlert = groupAlertDao.findByGroupKey(groupAlert.getGroupKey());
            // Process resolved alerts
            if (existGroupAlert != null) {
                List<String> existFingerprints = existGroupAlert.getAlertFingerprints();
                if (existFingerprints != null) {
                    alertFingerprints.addAll(existFingerprints);
                }
                // Merge group information
                groupAlert.setId(existGroupAlert.getId());
                groupAlert.setGmtCreate(existGroupAlert.getGmtCreate());
                // Merge other historical information to preserve
                Map<String, String> existCommonLabels = existGroupAlert.getCommonLabels();
                if (existCommonLabels != null) {
                    Map<String, String> commonLabels = groupAlert.getCommonLabels();
                    if (commonLabels != null) {
                        // filter common label in commonLabels and existCommonLabels
                        commonLabels = commonLabels.entrySet().stream()
                                .filter(entry -> existCommonLabels.containsKey(entry.getKey()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        groupAlert.setCommonLabels(commonLabels);
                    }
                }
                Map<String, String> existCommonAnnotations = existGroupAlert.getCommonAnnotations();
                if (existCommonAnnotations != null) {
                    Map<String, String> commonAnnotations = groupAlert.getCommonAnnotations();
                    if (commonAnnotations != null) {
                        // filter common annotation in commonAnnotations and existCommonAnnotations
                        commonAnnotations = commonAnnotations.entrySet().stream()
                                .filter(entry -> existCommonAnnotations.containsKey(entry.getKey()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        groupAlert.setCommonAnnotations(commonAnnotations);
                    }
                }
            }
            // Save alert group
            groupAlert.setAlertFingerprints(alertFingerprints.stream().toList());
            GroupAlert savedGroupAlert = groupAlertDao.save(groupAlert);
            savedGroupAlert.setAlerts(groupAlert.getAlerts());
            return savedGroupAlert;
        }
    }






    /**
     * 根据 fingerprintId 查询所有报警记录并批量更新
     *
     * @param fingerprintId 指纹 ID
     */
    private void updateAlertsByFingerprintId(String fingerprintId) {
        List<AlertHistory> alertList = alertHistoryDao.findAllByFingerprintId(fingerprintId);
        if (alertList == null || alertList.isEmpty()) {
            return; // 没有数据就直接返回
        }
        LocalDateTime currentDate = LocalDateTime.now();
        for (AlertHistory alert : alertList) {
            alert.setIsResolved(1); // 标记为已恢复
            alert.setResolvedTime(currentDate); // 使用统一时间
        }
        alertHistoryDao.saveAll(alertList);
    }


    /**
     * 保存报警历史记录
     */
    private String saveAlertHistory(SingleAlert singleAlert) {
        AlertHistory alertHistory = new AlertHistory();
        // 设置报警开始时间
        alertHistory.setCreateAlertTime(singleAlert.getGmtCreate());
        // 默认报警未恢复
        alertHistory.setIsResolved(0);
        // 设置报警描述内容
        alertHistory.setDescription(singleAlert.getContent());

        // 生成并设置报警指纹ID（哈希）
        String fingerprintId = generateMD5(singleAlert.getFingerprint());
        alertHistory.setFingerprintId(fingerprintId);
        // 解析 Labels 标签信息
        Map<String, String> alertInfoMap = singleAlert.getLabels();
        if (alertInfoMap != null) {
            alertHistory.setSeverity(alertInfoMap.get("severity")); //报警级别
            alertHistory.setComponentName(alertInfoMap.get("alertname")); //报警名称
            alertHistory.setMonitorId(Long.parseLong(alertInfoMap.get("instance"))); //报警监控组件ID
            alertHistory.setInstanceName(alertInfoMap.get("instancename")); //监控组件名称
            alertHistory.setIpAddress(alertInfoMap.get("instancehost"));  //报警地址
        }
        //不是报警恢复 新增数据
        if (!CommonConstants.ALERT_STATUS_RESOLVED.equals(singleAlert.getStatus())) {
            // 保存到数据库
            alertHistoryDao.save(alertHistory);
        }
        return fingerprintId;
    }


    /**
     * 生成MA5哈希ID
     * @param input 输入字符串
     * @return 输出
     */
    private String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());

            // 转16进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 算法不可用", e);
        }
    }


}
