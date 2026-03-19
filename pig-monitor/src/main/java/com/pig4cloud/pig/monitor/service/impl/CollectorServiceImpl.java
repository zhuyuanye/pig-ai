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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.pig4cloud.pig.common.core.entity.dto.CollectorSummary;
import com.pig4cloud.pig.common.core.entity.manager.Collector;
import com.pig4cloud.pig.common.core.entity.manager.CollectorMonitorBind;
import com.pig4cloud.pig.common.core.support.exception.CommonException;
import com.pig4cloud.pig.common.core.util.IpDomainUtil;
import com.pig4cloud.pig.monitor.mapper.CollectorMapper;
import com.pig4cloud.pig.monitor.mapper.CollectorMonitorBindMapper;
import com.pig4cloud.pig.monitor.scheduler.AssignJobs;
import com.pig4cloud.pig.monitor.scheduler.ConsistentHash;
import com.pig4cloud.pig.monitor.scheduler.netty.ManageServer;
import com.pig4cloud.pig.monitor.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * collector service impl
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CollectorServiceImpl implements CollectorService {

    @Autowired
    private CollectorMapper collectorMapper;

    @Autowired
    private CollectorMonitorBindMapper collectorMonitorBindMapper;

    @Autowired
    private ConsistentHash consistentHash;

    @Autowired(required = false)
    private ManageServer manageServer;

    @Override
    @Transactional(readOnly = true)
    public IPage<CollectorSummary> getCollectors(String name, int pageIndex, Integer pageSize) {
        if (pageSize == null) {
            pageSize = Integer.MAX_VALUE;
        }

        LambdaQueryWrapper<Collector> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.likeRight(Collector::getName, name.toLowerCase());
        }

        Page<Collector> page = new Page<>(pageIndex, pageSize);
        IPage<Collector> collectors = collectorMapper.selectPage(page, queryWrapper);

        List<CollectorSummary> collectorSummaryList = new LinkedList<>();
        for (Collector collector : collectors.getRecords()) {
            CollectorSummary.CollectorSummaryBuilder summaryBuilder = CollectorSummary.builder().collector(collector);
            ConsistentHash.Node node = consistentHash.getNode(collector.getName());
            if (node != null && node.getAssignJobs() != null) {
                AssignJobs assignJobs = node.getAssignJobs();
                summaryBuilder.pinMonitorNum(assignJobs.getPinnedJobs().size());
                summaryBuilder.dispatchMonitorNum(assignJobs.getJobs().size());
            }
            collectorSummaryList.add(summaryBuilder.build());
        }

        Page<CollectorSummary> resultPage = new Page<>(pageIndex, pageSize, collectors.getTotal());
        resultPage.setRecords(collectorSummaryList);
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRegisteredCollector(List<String> collectors) {
        if (CollectionUtils.isEmpty(collectors)) {
            return;
        }
        // Determine whether there are fixed tasks on the collector
        collectors.forEach(collector -> {
            List<CollectorMonitorBind> binds = this.collectorMonitorBindMapper.selectList(
                    new QueryWrapper<CollectorMonitorBind>().eq("collector", collector));
            if (CollectionUtils.isNotEmpty(binds)) {
                throw new CommonException("The collector " + collector + " has pinned tasks that cannot be deleted.");
            }
        });
        collectors.forEach(collector -> {
            this.manageServer.closeChannel(collector);
            this.collectorMapper.delete(new QueryWrapper<Collector>().eq("name", collector));
        });
    }

    @Override
    public boolean hasCollector(String collector) {
        return this.collectorMapper.selectOne(new QueryWrapper<Collector>().eq("name", collector)) != null;
    }

    @Override
    public Map<String, String> generateCollectorDeployInfo(String collector) {
        if (hasCollector(collector)) {
            throw new CommonException("There already exists a collector with same name.");
        }
        String host = IpDomainUtil.getLocalhostIp();
        Map<String, String> maps = new HashMap<>(6);
        maps.put("identity", collector);
        maps.put("host", host);
        return maps;
    }

    @Override
    public void makeCollectorsOffline(List<String> collectors) {
        if (CollectionUtils.isNotEmpty(collectors)) {
            collectors.forEach(collector -> this.manageServer.getCollectorAndJobScheduler().offlineCollector(collector));
        }
    }

    @Override
    public void makeCollectorsOnline(List<String> collectors) {
        if (CollectionUtils.isNotEmpty(collectors)) {
            collectors.forEach(collector ->
                    this.manageServer.getCollectorAndJobScheduler().onlineCollector(collector));
        }
    }
}
