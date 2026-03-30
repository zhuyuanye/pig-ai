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
import java.util.List;

/**
 * 告警分组收敛策略实体
 * <p>
 * 定义告警分组收敛的策略，包括分组标签、等待时间、发送间隔等。
 *
 * @author pig4cloud
 */
@TableName("hzb_alert_group_converge")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警分组收敛策略实体")
public class AlertGroupConverge {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "主键ID", example = "87584674384")
    private Long id;

    /**
     * 策略名称
     */
    @Schema(title = "策略名称", example = "group-converge-1")
    @Size(max = 100)
    @NotNull
    private String name;

    /**
     * 分组标签列表
     */
    @Schema(title = "分组标签列表", example = "[\"instance\"]")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonStringListTypeHandler.class)
    private List<String> groupLabels;

    /**
     * 首次发送分组告警前的等待时间（秒）
     */
    @Schema(title = "首次发送分组告警前的等待时间（秒）", example = "30")
    private Long groupWait;

    /**
     * 分组告警发送间隔（秒）
     */
    @Schema(title = "分组告警发送间隔（秒）", example = "300")
    private Long groupInterval;

    /**
     * 重复发送触发中告警的间隔（秒），设为0禁用重复发送
     */
    @Schema(title = "重复发送触发中告警的间隔（秒），设为0禁用重复发送", example = "9000")
    private Long repeatInterval;

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
