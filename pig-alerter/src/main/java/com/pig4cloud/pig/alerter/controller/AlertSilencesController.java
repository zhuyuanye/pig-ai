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
import com.pig4cloud.pig.common.alert.service.AlertSilenceService;
import com.pig4cloud.pig.common.core.entity.alerter.AlertSilence;
import com.pig4cloud.pig.common.core.entity.dto.Message;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 告警静默批量操作控制器
 * <p>提供告警静默规则的批量查询、批量删除等操作接口</p>
 *
 * @author pig4cloud
 */
@Tag(name = "Alert Silence Batch API")
@RestController
@RequestMapping(path = "/api/alert/silences", produces = {APPLICATION_JSON_VALUE})
public class AlertSilencesController {

    @Autowired
    private AlertSilenceService alertSilenceService;

    /**
     * 根据查询过滤条件获取告警静默规则列表（分页）
     *
     * @param ids       告警静默ID列表
     * @param search    搜索关键字
     * @param sort      排序字段，默认id
     * @param order     排序方式：asc升序，desc降序
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 告警静默规则分页列表
     */
    @GetMapping
    @Operation(summary = "Query the alarm silence list",
            description = "You can obtain the list of alarm silence by querying filter items")
    public ResponseEntity<Message<IPage<AlertSilence>>> getAlertSilences(
            @Parameter(description = "Alarm Silence ID", example = "6565463543") @RequestParam(required = false) List<Long> ids,
            @Parameter(description = "Search Name", example = "x") @RequestParam(required = false) String search,
            @Parameter(description = "Sort field, default id", example = "id") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Sort mode: asc: ascending, desc: descending", example = "desc") @RequestParam(defaultValue = "desc") String order,
            @Parameter(description = "List current page", example = "0") @RequestParam(defaultValue = "0") int pageIndex,
            @Parameter(description = "Number of list pages", example = "8") @RequestParam(defaultValue = "8") int pageSize) {
        IPage<AlertSilence> alertSilencePage = alertSilenceService.getAlertSilences(ids, search, sort, order, pageIndex, pageSize);
        return ResponseEntity.ok(Message.success(alertSilencePage));
    }

    /**
     * 根据告警静默ID列表批量删除告警静默规则
     *
     * @param ids 告警静默ID列表
     * @return 操作结果
     */
    @DeleteMapping
    @Operation(summary = "Delete alarm silence in batches",
            description = "Delete alarm silence in batches based on the alarm silence ID list")
    public ResponseEntity<Message<Void>> deleteAlertDefines(
            @Parameter(description = "Alarm Silence IDs", example = "6565463543") @RequestParam(required = false) List<Long> ids
    ) {
        if (ids != null && !ids.isEmpty()) {
            alertSilenceService.deleteAlertSilences(new HashSet<>(ids));
        }
        Message<Void> message = Message.success();
        return ResponseEntity.ok(message);
    }

}
