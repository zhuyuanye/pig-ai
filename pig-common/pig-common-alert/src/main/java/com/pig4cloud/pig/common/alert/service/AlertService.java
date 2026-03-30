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

package com.pig4cloud.pig.common.alert.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pig.common.alert.dto.AlertSummary;
import com.pig4cloud.pig.common.core.entity.alerter.GroupAlert;
import com.pig4cloud.pig.common.core.entity.alerter.SingleAlert;

import java.util.HashSet;
import java.util.List;

/**
 * 告警信息管理服务接口
 */
public interface AlertService {

    /**
     * 查询单条告警列表
     * @param status    告警状态
     * @param search    搜索关键字
     * @param sort      排序字段
     * @param order     排序方式
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 单条告警分页结果
     */
    IPage<SingleAlert> getSingleAlerts(String status, String search, String sort, String order, int pageIndex, int pageSize);

    /**
     * 动态条件查询分组告警
     * @param status    告警状态
     * @param search    搜索关键字
     * @param sort      排序字段
     * @param order     排序方式
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 分组告警分页结果
     */
    IPage<GroupAlert> getGroupAlerts(String status, String search, String sort, String order, int pageIndex, int pageSize, String startTime, String endTime);

    /**
     * 根据告警ID批量删除分组告警
     * @param ids 告警ID集合
     */
    void deleteGroupAlerts(HashSet<Long> ids);

    /**
     * 根据告警ID批量删除单条告警
     * @param ids 告警ID集合
     */
    void deleteSingleAlerts(HashSet<Long> ids);

    /**
     * 根据告警ID和状态值更新分组告警状态
     * @param status 要修改的告警状态
     * @param ids    要修改的告警ID列表
     */
    void editGroupAlertStatus(String status, List<Long> ids);

    /**
     * 根据告警ID和状态值更新单条告警状态
     * @param status 要修改的告警状态
     * @param ids    要修改的告警ID列表
     */
    void editSingleAlertStatus(String status, List<Long> ids);

    /**
     * 获取告警统计摘要信息
     * @return 告警统计摘要
     */
    AlertSummary getAlertsSummary();
}
