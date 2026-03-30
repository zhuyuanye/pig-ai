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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 * 告警分组实体
 * <p>
 * 将多条相关的单条告警聚合为一个告警组，
 * 通过 groupKey 唯一标识，包含公共标签和公共注解。
 *
 * @author pig4cloud
 */
@TableName("hzb_alert_group")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警分组实体")
public class GroupAlert {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "告警组ID", example = "87584674384", accessMode = READ_ONLY)
    private Long id;

    /**
     * 告警组唯一标识（唯一索引）
     */
    @Schema(title = "告警组Key", example = "HighCPUUsage{alertname=\"HighCPUUsage\"}")
    private String groupKey;

    /**
     * 告警组状态：firing（触发中）、resolved（已恢复）
     */
    @Schema(title = "告警状态", example = "resolved")
    private String status;

    /**
     * 分组标签
     */
    @Schema(title = "分组标签", example = "{\"alertname\": \"HighCPUUsage\"}")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
    private Map<String, String> groupLabels;

    /**
     * 公共标签（组内所有告警共有的标签）
     */
    @Schema(title = "公共标签", example = "{\"alertname\": \"HighCPUUsage\", \"severity\": \"critical\"}")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
    private Map<String, String> commonLabels;

    /**
     * 公共注解（组内所有告警共有的注解）
     */
    @Schema(title = "公共注解", example = "{\"summary\": \"High CPU usage detected\"}")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
    private Map<String, String> commonAnnotations;

    /**
     * 告警指纹列表，关联的单条告警 fingerprint
     */
    @Schema(title = "告警指纹列表", example = "[\"dxsdfdsf\"]")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonStringListTypeHandler.class)
    private List<String> alertFingerprints;

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
     * 关联的单条告警列表（非数据库字段，查询时填充）
     */
    @TableField(exist = false)
    private List<SingleAlert> alerts;
}
