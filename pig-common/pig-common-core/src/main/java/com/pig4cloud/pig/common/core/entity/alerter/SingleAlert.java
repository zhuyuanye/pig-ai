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

package com.pig4cloud.pig.common.core.entity.alerter;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.pig4cloud.pig.common.core.util.JsonUtil;

import java.time.LocalDateTime;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 * 单条告警实体
 * <p>
 * 记录单条告警的详细信息，包括标签、注解、状态、触发次数等。
 * 通过 fingerprint 唯一标识一条告警。
 *
 * @author pig4cloud
 */
@TableName("hzb_alert_single")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "单条告警实体")
public class SingleAlert {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "告警ID", example = "87584674384", accessMode = READ_ONLY)
    private Long id;

    /**
     * 告警指纹，用于唯一标识一条告警（唯一索引）
     */
    @Schema(title = "告警指纹", example = "alertname:demo")
    private String fingerprint;

    /**
     * 告警标签，如 {alertname: HighCPUUsage, priority: critical}
     */
    @Schema(title = "告警标签", example = "{\"alertname\": \"HighCPUUsage\"}")
    @TableField(typeHandler = com.pig4cloud.pig.common.mybatis.handler.JsonMapTypeHandler.class)
    private Map<String, String> labels;

    /**
     * 告警注解，如 {summary: 检测到高CPU使用率}
     */
    @Schema(title = "告警注解", example = "{\"summary\": \"High CPU usage detected\"}")
    @TableField(typeHandler = com.pig4cloud.pig.common.mybatis.handler.JsonMapTypeHandler.class)
    private Map<String, String> annotations;

    /**
     * 告警内容描述
     */
    @Schema(title = "告警内容", example = "CPU使用率超过80%")
    private String content;

    /**
     * 告警状态：firing（触发中）、resolved（已恢复）
     */
    @Schema(title = "告警状态", example = "firing|resolved")
    private String status;

    /**
     * 告警触发次数
     */
    @Schema(title = "触发次数", example = "1")
    private Integer triggerTimes;

    /**
     * 告警开始时间（毫秒时间戳）
     */
    @Schema(title = "开始时间（毫秒时间戳）", example = "1734005477630")
    private Long startAt;

    /**
     * 告警活跃时间（毫秒时间戳）
     */
    @Schema(title = "活跃时间（毫秒时间戳）", example = "1734005477630")
    private Long activeAt;

    /**
     * 告警结束时间，状态为 resolved 时有值
     */
    @Schema(title = "结束时间", example = "null")
    private Long endAt;

    /**
     * 创建者
     */
    @Schema(title = "创建者", example = "tom")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 最后修改者
     */
    @Schema(title = "最后修改者", example = "tom")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifier;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    /**
     * 最后修改时间
     */
    @Schema(title = "最后修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtUpdate;

    /**
     * 报警指纹ID
     */
    @Schema(title = "报警指纹ID", example = "id")
    private String fingerprintId;

    @Override
    public SingleAlert clone() {
        return JsonUtil.fromJson(JsonUtil.toJson(this), SingleAlert.class);
    }
}
