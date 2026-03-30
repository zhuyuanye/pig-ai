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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * 告警静默策略实体
 * <p>
 * 定义告警静默规则，支持一次性静默和周期性静默。
 * 可按标签匹配告警，并设置静默时间段。
 *
 * @author pig4cloud
 */
@TableName("hzb_alert_silence")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警静默策略实体")
public class AlertSilence {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "主键ID", example = "87584674384")
    private Long id;

    /**
     * 策略名称
     */
    @Schema(title = "策略名称", example = "silence-1")
    @Size(max = 100)
    @NotNull
    private String name;

    /**
     * 是否启用该策略
     */
    @Schema(title = "是否启用该策略", example = "true")
    private boolean enable = true;

    /**
     * 是否匹配所有告警
     */
    @Schema(title = "是否匹配所有告警", example = "true")
    private boolean matchAll = true;

    /**
     * 静默类型：0-一次性，1-周期性
     */
    @Schema(title = "静默类型：0-一次性，1-周期性", example = "1")
    @NotNull
    private Byte type;

    /**
     * 已静默告警次数
     */
    @Schema(title = "已静默告警次数", example = "3")
    private Integer times;

    /**
     * 匹配告警信息的标签
     */
    @Schema(description = "匹配告警信息的标签", example = "{name: key1, value: value1}")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
    private Map<String, String> labels;

    /**
     * 周期性静默生效的星期，多选，全部或空表示每天。7:周日 1:周一 2:周二 3:周三 4:周四 5:周五 6:周六
     */
    @Schema(title = "周期性静默生效的星期", example = "[0,1]")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonByteListTypeHandler.class)
    private List<Byte> days;

    /**
     * 限制时间段开始
     */
    @Schema(title = "限制时间段开始", example = "00:00:00")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.ZonedDateTimeTypeHandler.class)
    private ZonedDateTime periodStart;

    /**
     * 限制时间段结束
     */
    @Schema(title = "限制时间段结束", example = "23:59:59")
    @TableField(typeHandler = com.pig4cloud.pig.common.core.handler.ZonedDateTimeTypeHandler.class)
    private ZonedDateTime periodEnd;

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
