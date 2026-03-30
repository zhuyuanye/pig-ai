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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 通知模板实体
 * <p>
 * 定义通知消息的模板，支持多种通知方式。
 * 包含预设模板和自定义模板。
 *
 * @author pig4cloud
 */
@TableName("hzb_notice_template")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "通知策略模板实体")
public class NoticeTemplate {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "通知模板主键ID", description = "通知模板主键ID",
            example = "87584674384", accessMode = READ_ONLY)
    private Long id;

    /**
     * 模板名称
     */
    @Schema(title = "模板名称", description = "模板名称",
            example = "dispatch-1", accessMode = READ_WRITE)
    @Size(max = 100)
    @NotBlank
    private String name;

    /**
     * 通知方式：0-短信 1-邮件 2-webhook 3-微信公众号 4-企业微信机器人
     * 5-钉钉机器人 6-飞书机器人 7-Telegram Bot 8-SlackWebHook 9-Discord Bot 10-企业微信应用消息
     */
    @Schema(title = "通知方式", description = "通知方式：0-短信 1-邮件 2-webhook 3-微信公众号 4-企业微信机器人 "
            + "5-钉钉机器人 6-飞书机器人 7-Telegram Bot 8-SlackWebHook 9-Discord Bot 10-企业微信应用消息",
            accessMode = READ_WRITE)
    @Min(0)
    @NotNull
    private Byte type;

    /**
     * 是否为预设模板：true-预设模板 false-自定义模板
     */
    @Schema(title = "是否为预设模板", description = "是否为预设模板：true-预设模板 false-自定义模板",
            accessMode = READ_WRITE)
    private boolean preset = false;

    /**
     * 模板内容
     */
    @Schema(title = "模板内容", description = "模板内容",
            example = """
                    [${title}]
                    ${targetLabel} : ${target}
                    <#if (monitorId??)>${monitorIdLabel} : ${monitorId} </#if>
                    <#if (monitorName??)>${monitorNameLabel} : ${monitorName} </#if>
                    <#if (monitorHost??)>${monitorHostLabel} : ${monitorHost} </#if>
                    ${priorityLabel} : ${priority}
                    ${triggerTimeLabel} : ${triggerTime}
                    ${contentLabel} : ${content}""", accessMode = READ_WRITE)
    @Size(max = 60000)
    @NotBlank
    private String content;

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
