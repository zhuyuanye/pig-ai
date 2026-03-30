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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 通知策略实体
 * <p>
 * 定义通知规则，包括接收人、模板、过滤条件、生效时间等。
 * 支持按标签过滤、按星期和时间段限制通知。
 *
 * @author pig4cloud
 */
@TableName("hzb_notice_rule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "通知策略实体")
public class NoticeRule {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "通知策略主键ID", description = "通知策略主键ID",
            example = "87584674384", accessMode = READ_ONLY)
    private Long id;

    /**
     * 策略名称
     */
    @Schema(title = "策略名称", description = "策略名称",
            example = "dispatch-1", accessMode = READ_WRITE)
    @Size(max = 100)
    @NotBlank(message = "name can not null")
    private String name;

    /**
     * 接收人ID列表
     */
    @Schema(title = "接收人ID列表", description = "接收人ID列表",
            example = "4324324", accessMode = READ_WRITE)
    @NotEmpty(message = "receiverId can not empty")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonLongListTypeHandler.class)
    private List<Long> receiverId;

    /**
     * 接收人名称列表
     */
    @Schema(title = "接收人名称列表", description = "接收人名称列表",
            example = "tom", accessMode = READ_WRITE)
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonStringListTypeHandler.class)
    private List<String> receiverName;

    /**
     * 模板ID
     */
    @Schema(title = "模板ID", description = "模板ID",
            example = "4324324", accessMode = READ_WRITE)
    private Long templateId;

    /**
     * 模板名称
     */
    @Schema(title = "模板名称", description = "模板名称",
            example = "demo", accessMode = READ_WRITE)
    @Size(max = 100)
    private String templateName;

    /**
     * 是否启用该策略
     */
    @Schema(title = "是否启用该策略", description = "是否启用该策略",
            example = "true", accessMode = READ_WRITE)
    private boolean enable = true;

    /**
     * 是否转发所有告警
     */
    @Schema(title = "是否转发所有告警", description = "是否转发所有告警",
            example = "false", accessMode = READ_WRITE)
    private boolean filterAll = true;

    /**
     * 匹配标签
     */
    @Schema(title = "匹配标签", example = "{\"alertname\": \"HighCPUUsage\", \"priority\": \"critical\", \"instance\": \"343483943\"}")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
    private Map<String, String> labels;

    /**
     * 生效的星期，多选，全部或空表示每天。7:周日 1:周一 2:周二 3:周三 4:周四 5:周五 6:周六
     */
    @Schema(title = "生效的星期", example = "[0,1]", accessMode = READ_WRITE)
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonByteListTypeHandler.class)
    private List<Byte> days;

    /**
     * 限制时间段开始
     */
    @Schema(title = "限制时间段开始", example = "00:00:00", accessMode = READ_WRITE)
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.ZonedDateTimeTypeHandler.class)
    private ZonedDateTime periodStart;

    /**
     * 限制时间段结束
     */
    @Schema(title = "限制时间段结束", example = "23:59:59", accessMode = READ_WRITE)
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.ZonedDateTimeTypeHandler.class)
    private ZonedDateTime periodEnd;

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
