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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 告警规则定义实体
 * <p>
 * 定义告警触发条件、阈值表达式、标签、注解等信息。
 * 支持实时告警和周期性告警两种类型。
 *
 * @author pig4cloud
 */
@TableName("hzb_alert_define")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警规则定义实体")
public class AlertDefine {

    /**
     * 告警规则ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "告警规则ID", example = "87584674384", accessMode = READ_ONLY)
    private Long id;

    /**
     * 告警规则名称，需唯一
     */
    @Schema(title = "告警规则名称", example = "high_cpu_usage", accessMode = READ_WRITE)
    @Size(max = 100)
    @NotNull
    private String name;

    /**
     * 规则类型：realtime（实时）、periodic（周期性）
     */
    @Schema(title = "规则类型: realtime, periodic", example = "realtime")
    private String type;

    /**
     * 告警阈值表达式，如 usage>90
     */
    @Schema(title = "告警阈值表达式", example = "usage>90", accessMode = READ_WRITE)
    @Size(max = 2048)
    private String expr;

    /**
     * 执行周期（秒），仅周期性规则有效
     */
    @Schema(title = "执行周期（秒），仅周期性规则有效", example = "300")
    private Integer period;

    /**
     * 告警触发次数阈值，达到指定次数后才触发告警
     */
    @Schema(title = "告警触发次数阈值", example = "3", accessMode = READ_WRITE)
    private Integer times;

    /**
     * 标签键值对，如 {status:success, env:prod}
     */
    @Schema(description = "标签(status:success,env:prod)", example = "{name: key1, value: value1}", accessMode = READ_WRITE)
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
    private Map<String, String> labels;

    /**
     * 注解键值对，如 {summary: 高CPU使用率}
     */
    @Schema(title = "注解", example = "summary: High CPU usage")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
    private Map<String, String> annotations;

    /**
     * 告警内容模板，支持变量替换
     */
    @Schema(title = "告警内容模板", example = "实例 {{ $labels.instance }} CPU使用率 {{ $value }}%")
    @Size(max = 2048)
    private String template;

    /**
     * 数据源类型，如 PROMETHEUS
     */
    @Schema(title = "数据源类型", example = "PROMETHEUS")
    @Size(max = 100)
    private String datasource;

    /**
     * 是否启用该告警规则
     */
    @Schema(title = "是否启用", example = "true")
    private boolean enable = true;

    /**
     * 创建者
     */
    @Schema(title = "创建者", example = "tom", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 最后修改者
     */
    @Schema(title = "最后修改者", example = "tom", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifier;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 最后修改时间
     */
    @Schema(title = "最后修改时间", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtUpdate;
}
