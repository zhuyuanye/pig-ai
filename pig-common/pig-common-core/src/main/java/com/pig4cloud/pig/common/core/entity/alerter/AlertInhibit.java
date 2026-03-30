/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
import java.util.List;
import java.util.Map;

/**
 * 告警抑制规则实体
 * <p>
 * 定义告警抑制规则，当源告警匹配时抑制目标告警。
 * 通过 equalLabels 指定源和目标告警必须相同的标签。
 *
 * @author pig4cloud
 */
@TableName("hzb_alert_inhibit")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警抑制规则实体")
public class AlertInhibit {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "抑制规则ID", example = "1")
    private Long id;

    /**
     * 抑制规则名称
     */
    @Schema(title = "抑制规则名称", example = "inhibit_high_cpu")
    @Size(max = 100)
    @NotNull
    private String name;

    /**
     * 源告警匹配标签，当源告警匹配这些标签时抑制规则生效
     */
    @Schema(title = "源告警匹配标签")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
    private Map<String, String> sourceLabels;

    /**
     * 目标告警匹配标签，匹配这些标签的告警将被抑制
     */
    @Schema(title = "目标告警匹配标签")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
    private Map<String, String> targetLabels;

    /**
     * 相等标签列表，源和目标告警中这些标签的值必须相同才能触发抑制
     */
    @Schema(title = "相等标签列表")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonStringListTypeHandler.class)
    private List<String> equalLabels;

    /**
     * 是否启用该策略
     */
    @Schema(title = "是否启用该策略", example = "true")
    private Boolean enable;

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
    private LocalDateTime gmtCreate;

    /**
     * 最后修改时间
     */
    @Schema(title = "最后修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtUpdate;
}
