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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.pig4cloud.pig.common.core.entity.manager.Bulletin;
import com.pig4cloud.pig.common.core.entity.manager.Monitor;
import com.pig4cloud.pig.common.core.entity.message.CollectRep;
import com.pig4cloud.pig.monitor.mapper.BulletinMapper;
import com.pig4cloud.pig.monitor.pojo.dto.BulletinMetricsData;
import com.pig4cloud.pig.monitor.service.BulletinService;
import com.pig4cloud.pig.monitor.service.MonitorService;
import com.pig4cloud.pig.common.warehouse.store.realtime.RealTimeDataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.*;

/**
 * Bulletin Service Implementation
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BulletinServiceImpl implements BulletinService {

    private static final String NO_DATA = "No Data";

    private static final String EMPTY_STRING = "";

    @Autowired
    private BulletinMapper bulletinMapper;

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private RealTimeDataReader realTimeDataReader;

    /**
     * validate Bulletin
     */
    @Override
    public void validate(Bulletin bulletin) throws IllegalArgumentException {
        if (bulletin == null) {
            throw new IllegalArgumentException("Bulletin cannot be null");
        }
        if (bulletin.getApp() == null || bulletin.getApp().isEmpty()) {
            throw new IllegalArgumentException("Bulletin app cannot be null or empty");
        }
        if (bulletin.getFields() == null || bulletin.getFields().isEmpty()) {
            throw new IllegalArgumentException("Bulletin fields cannot be null or empty");
        }
        if (bulletin.getMonitorIds() == null || bulletin.getMonitorIds().isEmpty()) {
            throw new IllegalArgumentException("Bulletin monitorIds cannot be null or empty");
        }
        Long count = bulletinMapper.selectCount(new QueryWrapper<Bulletin>().eq("name", bulletin.getName()));
        if (count != null && count > 0) {
            throw new IllegalArgumentException("Bulletin name duplicated");
        }
    }

    /**
     * Save Bulletin
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editBulletin(Bulletin bulletin) {
        Bulletin existing = bulletinMapper.selectById(bulletin.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Bulletin not found");
        }
        bulletinMapper.updateById(bulletin);
    }

    /**
     * Add Bulletin
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBulletin(Bulletin bulletin) {
        bulletinMapper.insert(bulletin);
    }

    /**
     * deal with the bulletin
     */
    @Override
    public BulletinMetricsData buildBulletinMetricsData(Long id) {
        Bulletin bulletin = bulletinMapper.selectById(id);
        if (bulletin == null) {
            throw new IllegalArgumentException("Bulletin not found");
        }
        BulletinMetricsData.BulletinMetricsDataBuilder contentBuilder = BulletinMetricsData.builder()
                .name(bulletin.getName());
        List<BulletinMetricsData.Data> dataList = new ArrayList<>();
        for (Long monitorId : bulletin.getMonitorIds()) {
            Monitor monitor = monitorService.getMonitor(monitorId);
            BulletinMetricsData.Data.DataBuilder dataBuilder = BulletinMetricsData.Data.builder()
                    .monitorId(monitorId)
                    .monitorName(monitor.getName())
                    .host(monitor.getHost());

            List<BulletinMetricsData.Metric> metrics = new ArrayList<>();
            Map<String, List<String>> fieldMap = bulletin.getFields();

            if (fieldMap != null) {
                for (Map.Entry<String, List<String>> entry : fieldMap.entrySet()) {
                    String metric = entry.getKey();
                    List<String> fields = entry.getValue();
                    BulletinMetricsData.Metric.MetricBuilder metricBuilder = BulletinMetricsData.Metric.builder()
                            .name(metric);
                    CollectRep.MetricsData currentMetricsData = realTimeDataReader.getCurrentMetricsData(monitorId, metric);

                    List<List<BulletinMetricsData.Field>> fieldsList;
                    if (currentMetricsData != null) {
                        fieldsList = currentMetricsData.getValues().stream()
                                .map(valueRow -> {
                                    List<BulletinMetricsData.Field> fieldList = currentMetricsData.getFields().stream()
                                            .map(field -> BulletinMetricsData.Field.builder()
                                                    .key(field.getName())
                                                    .unit(field.getUnit())
                                                    .build())
                                            .toList();

                                    for (int i = 0; i < fieldList.size(); i++) {
                                        fieldList.get(i).setValue(valueRow.getColumns(i));
                                    }
                                    return fieldList.stream().filter(field -> fields.contains(field.getKey())).toList();
                                })
                                .toList();
                    } else {
                        fieldsList = Collections.singletonList(fields.stream()
                                .map(field -> BulletinMetricsData.Field.builder()
                                        .key(field)
                                        .unit(EMPTY_STRING)
                                        .value(NO_DATA)
                                        .build())
                                .toList());
                    }

                    metricBuilder.fields(fieldsList);
                    metrics.add(metricBuilder.build());
                }
            }
            dataBuilder.metrics(metrics);
            dataList.add(dataBuilder.build());
        }
        contentBuilder.content(dataList);
        return contentBuilder.build();
    }

    @Override
    public IPage<Bulletin> getBulletins(String search, Integer pageIndex, Integer pageSize) {
        pageIndex = pageIndex == null ? 0 : pageIndex;
        pageSize = pageSize == null ? Integer.MAX_VALUE : pageSize;

        LambdaQueryWrapper<Bulletin> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(search)) {
            queryWrapper.like(Bulletin::getName, search);
        }

        Page<Bulletin> page = new Page<>(pageIndex, pageSize);
        return bulletinMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void deleteBulletins(List<Long> ids) {
        bulletinMapper.deleteBatchIds(ids);
    }

    /**
     * Get Bulletin by id
     */
    @Override
    public Bulletin getBulletinById(Long id) {
        return bulletinMapper.selectById(id);
    }
}
