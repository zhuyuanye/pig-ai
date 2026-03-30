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

package com.pig4cloud.pig.alerter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.pig4cloud.pig.common.alert.dto.AlertSummary;
import com.pig4cloud.pig.common.alert.service.AlertService;
import com.pig4cloud.pig.common.core.entity.alerter.GroupAlert;
import com.pig4cloud.pig.common.core.entity.alerter.SingleAlert;
import com.pig4cloud.pig.common.core.entity.dto.Message;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 告警管理批量操作控制器
 * <p>提供告警的批量查询、批量删除、状态修改、统计汇总等操作接口</p>
 *
 * @author pig4cloud
 */
@Tag(name = "Alarm Manage Batch API")
@RestController
@RequestMapping(path = "/api/alerts", produces = {APPLICATION_JSON_VALUE})
public class AlertsController {

    @Autowired
    private AlertService alertService;

    /**
     * 查询单条告警列表（分页）
     *
     * @param status    告警状态
     * @param search    告警内容模糊查询关键字
     * @param sort      排序字段，默认gmtUpdate
     * @param order     排序方式：asc升序，desc降序
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 单条告警分页列表
     */
    @GetMapping
    @Operation(summary = "Query Alarms")
    public ResponseEntity<Message<IPage<SingleAlert>>> getAlerts(
            @Parameter(description = "Alarm Status", example = "resolved") @RequestParam(required = false) String status,
            @Parameter(description = "Alarm content fuzzy query", example = "linux") @RequestParam(required = false) String search,
            @Parameter(description = "Sort field, default id", example = "name") @RequestParam(defaultValue = "gmtUpdate") String sort,
            @Parameter(description = "Sort Type", example = "desc") @RequestParam(defaultValue = "desc") String order,
            @Parameter(description = "List current page", example = "0") @RequestParam(defaultValue = "0") int pageIndex,
            @Parameter(description = "Number of list pagination", example = "8") @RequestParam(defaultValue = "8") int pageSize) {
        IPage<SingleAlert> alertPage = alertService.getSingleAlerts(status, search, sort, order, pageIndex, pageSize);
        return ResponseEntity.ok(Message.success(alertPage));
    }

    /**
     * 查询分组告警列表（分页）
     *
     * @param status    告警状态
     * @param search    告警内容模糊查询关键字
     * @param sort      排序字段，默认gmtUpdate
     * @param order     排序方式：asc升序，desc降序
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 分组告警分页列表
     */
    @GetMapping("/group")
    @Operation(summary = "Query Group Alarms")
    public ResponseEntity<Message<IPage<GroupAlert>>> getGroupAlerts(
            @Parameter(description = "Alarm Status", example = "resolved") @RequestParam(required = false) String status,
            @Parameter(description = "Alarm content fuzzy query", example = "linux") @RequestParam(required = false) String search,
            @Parameter(description = "Sort field, default id", example = "name") @RequestParam(defaultValue = "gmtUpdate") String sort,
            @Parameter(description = "Sort Type", example = "desc") @RequestParam(defaultValue = "desc") String order,
            @Parameter(description = "List current page", example = "0") @RequestParam(defaultValue = "0") int pageIndex,
            @Parameter(description = "Number of list pagination", example = "8") @RequestParam(defaultValue = "8") int pageSize,
            @Parameter(description = "开始时间", example = "2025-04-23 00:00:00")  String startTime,
            @Parameter(description = "结束时间", example = "2025-04-23 00:00:00")  String endTime
    ) {
        IPage<GroupAlert> alertPage = alertService.getGroupAlerts(status, search, sort, order, pageIndex, pageSize, startTime, endTime);
        return ResponseEntity.ok(Message.success(alertPage));
    }

    /**
     * 批量删除分组告警
     *
     * @param ids 告警ID列表
     * @return 操作结果
     */
    @DeleteMapping("/group")
    @Operation(summary = "Delete group alarms in batches", description = "according to the alarm ID list to delete the alarm information in batches")
    public ResponseEntity<Message<Void>> deleteAlerts(
            @Parameter(description = "Alarm List ID", example = "6565463543") @RequestParam(required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            alertService.deleteGroupAlerts(new HashSet<>(ids));
        }
        Message<Void> message = Message.success();
        return ResponseEntity.ok(message);
    }

    /**
     * 批量修改分组告警状态（设置为firing或resolved）
     *
     * @param status 告警状态值
     * @param ids    告警ID列表
     * @return 操作结果
     */
    @PutMapping(path = "/group/status/{status}")
    @Operation(summary = "Batch modify alarm status, set firing or resolved", description = "Batch modify alarm status, set firing or resolved")
    public ResponseEntity<Message<Void>> applyAlertDefinesStatus(
            @Parameter(description = "Alarm status value", example = "resolved") @PathVariable String status,
            @Parameter(description = "Alarm List IDS", example = "6565463543") @RequestParam(required = false) List<Long> ids) {
        if (ids != null && status != null && !ids.isEmpty()) {
            alertService.editGroupAlertStatus(status, ids);
        }
        Message<Void> message = Message.success();
        return ResponseEntity.ok(message);
    }

    /**
     * 获取告警统计汇总信息
     *
     * @return 告警统计信息
     */
    @GetMapping(path = "/summary")
    @Operation(summary = "Get alarm statistics", description = "Get alarm statistics information")
    public ResponseEntity<Message<AlertSummary>> getAlertsSummary() {
        AlertSummary alertSummary = alertService.getAlertsSummary();
        Message<AlertSummary> message = Message.success(alertSummary);
        return ResponseEntity.ok(message);
    }

}
