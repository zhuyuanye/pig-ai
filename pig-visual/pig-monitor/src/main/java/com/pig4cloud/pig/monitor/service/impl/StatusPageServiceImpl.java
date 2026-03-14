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

package com.pig4cloud.pig.monitor.service.impl;

import lombok.RequiredArgsConstructor;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.entity.manager.StatusPageComponent;
import com.pig4cloud.pig.common.core.entity.manager.StatusPageHistory;
import com.pig4cloud.pig.common.core.entity.manager.StatusPageIncident;
import com.pig4cloud.pig.common.core.entity.manager.StatusPageOrg;
import com.pig4cloud.pig.common.core.support.exception.CommonException;
import com.pig4cloud.pig.monitor.component.status.CalculateStatus;
import com.pig4cloud.pig.monitor.mapper.*;
import com.pig4cloud.pig.monitor.pojo.dto.ComponentStatus;
import com.pig4cloud.pig.monitor.service.StatusPageService;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * status page service implement.
 */
@Service
@RequiredArgsConstructor
public class StatusPageServiceImpl implements StatusPageService {

    private static final int HISTORY_SPAN_DAYS = 29;

    @Autowired
    private StatusPageOrgMapper statusPageOrgMapper;

    @Autowired
    private StatusPageComponentMapper statusPageComponentMapper;

    @Autowired
    private StatusPageHistoryMapper statusPageHistoryMapper;

    @Autowired
    private StatusPageIncidentMapper statusPageIncidentMapper;

    @Autowired
    private CalculateStatus calculateStatus;

    private final StatusPageIncidentComponentBindMapper statusPageIncidentComponentBindMapper;


    @Override
    public StatusPageOrg queryStatusPageOrg() {
        List<StatusPageOrg> list = statusPageOrgMapper.selectList(null);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public StatusPageOrg saveStatusPageOrg(StatusPageOrg statusPageOrg) {
        if (statusPageOrg.getId() != null) {
            statusPageOrgMapper.updateById(statusPageOrg);
        } else {
            statusPageOrgMapper.insert(statusPageOrg);
        }
        return statusPageOrg;
    }

    @Override
    public List<StatusPageComponent> queryStatusPageComponents() {
        return statusPageComponentMapper.selectList(null);
    }

    @Override
    public void newStatusPageComponent(StatusPageComponent statusPageComponent) {
        if (statusPageComponent.getMethod() == CommonConstants.STATUS_PAGE_CALCULATE_METHOD_MANUAL) {
            statusPageComponent.setState(statusPageComponent.getConfigState());
        }
        statusPageComponentMapper.insert(statusPageComponent);
    }

    @Override
    public void updateStatusPageComponent(StatusPageComponent statusPageComponent) {
        if (statusPageComponent.getMethod() == CommonConstants.STATUS_PAGE_CALCULATE_METHOD_MANUAL) {
            statusPageComponent.setState(statusPageComponent.getConfigState());
        }
        statusPageComponentMapper.updateById(statusPageComponent);
    }

    @Override
    public void deleteStatusPageComponent(long id) {
        Long count = statusPageIncidentComponentBindMapper.selectCount(
                new QueryWrapper<StatusPageIncidentComponentBind>().eq("component_id", id));
        if (count != null && count != 0) {
            throw new CommonException("The component is associated with an event and cannot be deleted. Please delete the event and try again!");
        }
        statusPageComponentMapper.deleteById(id);
    }

    @Override
    public StatusPageComponent queryStatusPageComponent(long id) {
        return statusPageComponentMapper.selectById(id);
    }

    @Override
    public List<ComponentStatus> queryComponentsStatus() {
        List<StatusPageComponent> components = statusPageComponentMapper.selectList(null);
        List<ComponentStatus> componentStatusList = new LinkedList<>();
        for (StatusPageComponent component : components) {
            ComponentStatus componentStatus = new ComponentStatus();
            componentStatus.setInfo(component);
            List<StatusPageHistory> histories = new LinkedList<>();
            // query today status
            LocalDateTime nowTime = LocalDateTime.now();
            LocalDateTime todayStartTime = nowTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            long nowTimestamp = nowTime.toInstant(zoneOffset).toEpochMilli();
            long todayStartTimestamp = todayStartTime.toInstant(zoneOffset).toEpochMilli();
            List<StatusPageHistory> todayStatusPageHistoryList = statusPageHistoryMapper.selectList(
                    new QueryWrapper<StatusPageHistory>()
                            .eq("component_id", component.getId())
                            .ge("timestamp", todayStartTimestamp)
                            .le("timestamp", nowTimestamp));
            StatusPageHistory todayStatus = combineOneDayStatusPageHistory(todayStatusPageHistoryList, component, nowTimestamp);
            histories.add(todayStatus);
            // query 30d component status history
            LocalDateTime preTime = todayStartTime.minusDays(HISTORY_SPAN_DAYS);
            long preTimestamp = preTime.toInstant(zoneOffset).toEpochMilli();
            List<StatusPageHistory> history = statusPageHistoryMapper.selectList(
                    new QueryWrapper<StatusPageHistory>()
                            .eq("component_id", component.getId())
                            .ge("timestamp", preTimestamp)
                            .le("timestamp", todayStartTimestamp));
            LinkedList<StatusPageHistory> historyList = new LinkedList<>(history);
            historyList.sort((o1, o2) -> (int) (o1.getTimestamp() - o2.getTimestamp()));
            LocalDateTime endTime = todayStartTime.minusSeconds(1);
            LocalDateTime startTime = endTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
            for (int index = 0; index < HISTORY_SPAN_DAYS; index++) {
                long startTimestamp = startTime.toInstant(zoneOffset).toEpochMilli();
                long endTimestamp = endTime.toInstant(zoneOffset).toEpochMilli();
                List<StatusPageHistory> thisDayHistory = historyList.stream().filter(item ->
                                item.getTimestamp() >= startTimestamp && item.getTimestamp() <= endTimestamp)
                        .collect(Collectors.toList());
                if (thisDayHistory.isEmpty()) {
                    StatusPageHistory statusPageHistory = StatusPageHistory.builder().timestamp(endTimestamp)
                            .componentId(component.getId()).state(CommonConstants.STATUS_PAGE_COMPONENT_STATE_UNKNOWN).build();
                    histories.add(statusPageHistory);
                } else if (thisDayHistory.size() == 1) {
                    histories.add(thisDayHistory.get(0));
                } else {
                    StatusPageHistory statusPageHistory = combineOneDayStatusPageHistory(thisDayHistory, component, endTimestamp);
                    histories.add(statusPageHistory);
                    thisDayHistory.forEach(historyItem -> statusPageHistoryMapper.deleteById(historyItem.getId()));
                    statusPageHistoryMapper.insert(statusPageHistory);
                }
                startTime = startTime.minusDays(1);
                endTime = endTime.minusDays(1);
            }
            componentStatus.setHistory(histories);
            componentStatusList.add(componentStatus);
        }
        return componentStatusList;
    }

    private StatusPageHistory combineOneDayStatusPageHistory(List<StatusPageHistory> statusPageHistories, StatusPageComponent component, long nowTimestamp) {
        if (statusPageHistories.isEmpty()) {
            return StatusPageHistory.builder().timestamp(nowTimestamp)
                    .normal(0).abnormal(0).unknowing(0).componentId(component.getId()).state(component.getState()).build();
        }
        if (statusPageHistories.size() == 1) {
            return statusPageHistories.get(0);
        }
        StatusPageHistory oldOne = statusPageHistories.get(0);
        StatusPageHistory todayStatus = StatusPageHistory.builder().timestamp(nowTimestamp)
                .normal(0).abnormal(0).unknowing(0).gmtCreate(oldOne.getGmtCreate()).gmtUpdate(oldOne.getGmtUpdate())
                .componentId(component.getId()).state(component.getState()).build();
        for (StatusPageHistory statusPageHistory : statusPageHistories) {
            if (statusPageHistory.getState() == CommonConstants.STATUS_PAGE_COMPONENT_STATE_ABNORMAL) {
                todayStatus.setAbnormal(todayStatus.getAbnormal() + calculateStatus.getCalculateStatusIntervals());
            } else if (statusPageHistory.getState() == CommonConstants.STATUS_PAGE_COMPONENT_STATE_UNKNOWN) {
                todayStatus.setUnknowing(todayStatus.getUnknowing() + calculateStatus.getCalculateStatusIntervals());
            } else {
                todayStatus.setNormal(todayStatus.getNormal() + calculateStatus.getCalculateStatusIntervals());
            }
        }
        double total = todayStatus.getNormal() + todayStatus.getAbnormal() + todayStatus.getUnknowing();
        double uptime = 0;
        if (total > 0) {
            uptime = (double) todayStatus.getNormal() / total;
        }
        todayStatus.setUptime(uptime);
        if (todayStatus.getAbnormal() > 0) {
            todayStatus.setState(CommonConstants.STATUS_PAGE_COMPONENT_STATE_ABNORMAL);
        } else if (todayStatus.getNormal() > 0) {
            todayStatus.setState(CommonConstants.STATUS_PAGE_COMPONENT_STATE_NORMAL);
        } else {
            todayStatus.setState(CommonConstants.STATUS_PAGE_COMPONENT_STATE_UNKNOWN);
        }
        return todayStatus;
    }

    @Override
    public ComponentStatus queryComponentStatus(long id) {
        StatusPageComponent component = statusPageComponentMapper.selectById(id);
        if (component == null) {
            throw new IllegalArgumentException("component not found");
        }
        ComponentStatus componentStatus = new ComponentStatus();
        componentStatus.setInfo(component);
        List<StatusPageHistory> histories = new LinkedList<>();
        // query today status
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime todayStartTime = nowTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        long nowTimestamp = nowTime.toInstant(zoneOffset).toEpochMilli();
        long todayStartTimestamp = todayStartTime.toInstant(zoneOffset).toEpochMilli();
        List<StatusPageHistory> todayStatusPageHistoryList = statusPageHistoryMapper.selectList(
                new QueryWrapper<StatusPageHistory>()
                        .eq("component_id", component.getId())
                        .ge("timestamp", todayStartTimestamp)
                        .le("timestamp", nowTimestamp));
        StatusPageHistory todayStatus = combineOneDayStatusPageHistory(todayStatusPageHistoryList, component, nowTimestamp);
        histories.add(todayStatus);
        // query 30d component status history
        LocalDateTime preTime = todayStartTime.minusDays(HISTORY_SPAN_DAYS);
        long preTimestamp = preTime.toInstant(zoneOffset).toEpochMilli();
        List<StatusPageHistory> history = statusPageHistoryMapper.selectList(
                new QueryWrapper<StatusPageHistory>()
                        .eq("component_id", component.getId())
                        .ge("timestamp", preTimestamp)
                        .le("timestamp", todayStartTimestamp));
        LinkedList<StatusPageHistory> historyList = new LinkedList<>(history);
        historyList.sort((o1, o2) -> (int) (o1.getTimestamp() - o2.getTimestamp()));
        LocalDateTime endTime = todayStartTime.minusSeconds(1);
        LocalDateTime startTime = endTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        for (int index = 0; index < HISTORY_SPAN_DAYS; index++) {
            long startTimestamp = startTime.toInstant(zoneOffset).toEpochMilli();
            long endTimestamp = endTime.toInstant(zoneOffset).toEpochMilli();
            List<StatusPageHistory> thisDayHistory = historyList.stream().filter(item ->
                            item.getTimestamp() >= startTimestamp && item.getTimestamp() <= endTimestamp)
                    .collect(Collectors.toList());
            if (thisDayHistory.isEmpty()) {
                StatusPageHistory statusPageHistory = StatusPageHistory.builder().timestamp(endTimestamp)
                        .componentId(component.getId()).state(CommonConstants.STATUS_PAGE_COMPONENT_STATE_UNKNOWN).build();
                histories.add(statusPageHistory);
            } else if (thisDayHistory.size() == 1) {
                histories.add(thisDayHistory.get(0));
            } else {
                StatusPageHistory statusPageHistory = combineOneDayStatusPageHistory(thisDayHistory, component, endTimestamp);
                histories.add(statusPageHistory);
                thisDayHistory.forEach(historyItem -> statusPageHistoryMapper.deleteById(historyItem.getId()));
                statusPageHistoryMapper.insert(statusPageHistory);
            }
            startTime = startTime.minusDays(1);
            endTime = endTime.minusDays(1);
        }
        componentStatus.setHistory(histories);
        return componentStatus;
    }

    @Override
    public List<StatusPageIncident> queryStatusPageIncidents() {
        return statusPageIncidentMapper.selectList(
                new QueryWrapper<StatusPageIncident>().orderByDesc("start_time"));
    }

    @Override
    public StatusPageIncident queryStatusPageIncident(long id) {
        return statusPageIncidentMapper.selectById(id);
    }

    @Override
    public void newStatusPageIncident(StatusPageIncident statusPageIncident) {
        statusPageIncident.setStartTime(System.currentTimeMillis());
        if (statusPageIncident.getState() == CommonConstants.STATUS_PAGE_INCIDENT_STATE_RESOLVED) {
            statusPageIncident.setEndTime(System.currentTimeMillis());
        }
        statusPageIncidentMapper.insert(statusPageIncident);
    }

    @Override
    public void updateStatusPageIncident(StatusPageIncident statusPageIncident) {
        if (statusPageIncident.getState() == CommonConstants.STATUS_PAGE_INCIDENT_STATE_RESOLVED) {
            statusPageIncident.setEndTime(System.currentTimeMillis());
        }
        statusPageIncidentMapper.updateById(statusPageIncident);
    }

    @Override
    public void deleteStatusPageIncident(long id) {
        statusPageIncidentMapper.deleteById(id);
    }
}
