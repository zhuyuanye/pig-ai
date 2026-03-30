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
 * 消息通知接收人实体
 * <p>
 * 定义通知接收人的信息，支持多种通知方式：
 * 短信、邮件、Webhook、微信、钉钉、飞书、Telegram等。
 *
 * @author pig4cloud
 */
@TableName("hzb_notice_receiver")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息通知接收人实体")
public class NoticeReceiver {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "接收人主键ID", description = "接收人主键ID",
            example = "87584674384", accessMode = READ_ONLY)
    private Long id;

    /**
     * 接收人名称
     */
    @Schema(title = "接收人名称", description = "接收人名称",
            example = "tom", accessMode = READ_WRITE)
    @Size(max = 100)
    @NotBlank(message = "name can not null")
    private String name;

    /**
     * 通知方式：0-短信 1-邮件 2-webhook 3-微信公众号 4-企业微信机器人
     * 5-钉钉机器人 6-飞书机器人 7-Telegram Bot 8-SlackWebHook 9-Discord Bot 10-企业微信应用消息
     */
    @Schema(title = "通知方式", description = "通知方式：0-短信 1-邮件 2-webhook 3-微信公众号 4-企业微信机器人 "
            + "5-钉钉机器人 6-飞书机器人 7-Telegram Bot 8-SlackWebHook 9-Discord Bot 10-企业微信应用消息",
            accessMode = READ_WRITE)
    @Min(0)
    @NotNull(message = "type can not null")
    private Byte type;

    /**
     * 手机号码，通知方式为短信时有效
     */
    @Schema(title = "手机号码", description = "通知方式为短信时有效",
            example = "18923435643", accessMode = READ_WRITE)
    @Size(max = 100)
    private String phone;

    /**
     * 邮箱地址，通知方式为邮件时有效
     */
    @Schema(title = "邮箱地址", description = "通知方式为邮件时有效",
            example = "tom@qq.com", accessMode = READ_WRITE)
    @Size(max = 100)
    private String email;

    /**
     * Webhook地址，通知方式为webhook时有效
     */
    @Schema(title = "Webhook地址", description = "通知方式为webhook时有效",
            example = "https://www.tancloud.cn", accessMode = READ_WRITE)
    @Size(max = 300)
    private String hookUrl;

    /**
     * 微信ID，通知方式为微信公众号、企业微信机器人或飞书机器人时有效
     */
    @Schema(title = "微信ID", description = "通知方式为微信公众号、企业微信机器人或飞书机器人时有效",
            example = "343432", accessMode = READ_WRITE)
    @Size(max = 300)
    private String wechatId;

    /**
     * 访问令牌，通知方式为钉钉机器人时有效
     */
    @Schema(title = "访问令牌", description = "通知方式为钉钉机器人时有效",
            example = "34823984635647", accessMode = READ_WRITE)
    @Size(max = 300)
    private String accessToken;

    /**
     * Telegram Bot Token
     */
    @Schema(title = "Telegram Bot Token", description = "通知方式为Telegram Bot时有效",
            example = "1499012345:AAEOB_wEYS-DZyPM3h5NzI8voJMXXXXXX", accessMode = READ_WRITE)
    private String tgBotToken;

    /**
     * Telegram用户ID
     */
    @Schema(title = "Telegram用户ID", description = "通知方式为Telegram Bot时有效",
            example = "779294123", accessMode = READ_WRITE)
    private String tgUserId;

    /**
     * Telegram消息线程ID
     */
    @Schema(title = "Telegram消息线程ID", description = "通知方式为Telegram Bot时有效",
            example = "779294123", accessMode = READ_WRITE)
    private String tgMessageThreadId;

    /**
     * 钉钉/飞书/企业微信用户ID
     */
    @Schema(title = "钉钉/飞书/企业微信用户ID", description = "通知方式为钉钉、飞书、企业微信Bot时有效",
            example = "779294123", accessMode = READ_WRITE)
    private String userId;

    /**
     * Slack Webhook地址
     */
    @Schema(title = "Slack Webhook地址", description = "通知方式为Slack时有效",
            example = "https://hooks.slack.com/services/XXXX/XXXX/XXXX", accessMode = READ_WRITE)
    @Size(max = 300)
    private String slackWebHookUrl;

    /**
     * 企业微信企业ID
     */
    @Schema(title = "企业微信企业ID", description = "通知方式为企业微信应用消息时有效",
            example = "ww1a603432123d0dc1", accessMode = READ_WRITE)
    private String corpId;

    /**
     * 企业微信应用ID
     */
    @Schema(title = "企业微信应用ID", description = "通知方式为企业微信应用消息时有效",
            example = "1000001", accessMode = READ_WRITE)
    private Integer agentId;

    /**
     * 企业微信应用密钥
     */
    @Schema(title = "企业微信应用密钥", description = "通知方式为企业微信应用消息时有效",
            example = "oUydwn92ey0lnuY02MixNa57eNK-20dJn5NEOG-u2uE", accessMode = READ_WRITE)
    private String appSecret;

    /**
     * 企业微信部门ID
     */
    @Schema(title = "企业微信部门ID", description = "通知方式为企业微信应用消息时有效",
            example = "779294123", accessMode = READ_WRITE)
    private String partyId;

    /**
     * 企业微信标签ID
     */
    @Schema(title = "企业微信标签ID", description = "通知方式为企业微信应用消息时有效",
            example = "779294123", accessMode = READ_WRITE)
    private String tagId;

    /**
     * Discord频道ID
     */
    @Schema(title = "Discord频道ID", description = "通知方式为Discord时有效",
            example = "1065303416030642266", accessMode = READ_WRITE)
    @Size(max = 300)
    private String discordChannelId;

    /**
     * Discord Bot Token
     */
    @Schema(title = "Discord Bot Token", description = "通知方式为Discord时有效",
            example = "MTA2NTMwMzU0ODY4Mzg4MjUzNw.xxxxx.xxxxxxx", accessMode = READ_WRITE)
    @Size(max = 300)
    private String discordBotToken;

    /**
     * 华为云SMN AK
     */
    @Schema(title = "华为云SMN AK", description = "通知方式为华为云SMN时有效",
            example = "NCVBODJOEYHSW3VNXXXX", accessMode = READ_WRITE)
    @Size(max = 22)
    private String smnAk;

    /**
     * 华为云SMN SK
     */
    @Schema(title = "华为云SMN SK", description = "通知方式为华为云SMN时有效",
            example = "nmSNhUJN9MlpPl8lfCsgdA0KvHCL9JXXXX", accessMode = READ_WRITE)
    @Size(max = 42)
    private String smnSk;

    /**
     * 华为云SMN项目ID
     */
    @Schema(title = "华为云SMN项目ID", description = "通知方式为华为云SMN时有效",
            example = "320c2fb11edb47a481c299c1XXXXXX", accessMode = READ_WRITE)
    @Size(max = 32)
    private String smnProjectId;

    /**
     * 华为云SMN区域
     */
    @Schema(title = "华为云SMN区域", description = "通知方式为华为云SMN时有效",
            example = "cn-east-3", accessMode = READ_WRITE)
    @Size(max = 32)
    private String smnRegion;

    /**
     * 华为云SMN主题URN
     */
    @Schema(title = "华为云SMN主题URN", description = "通知方式为华为云SMN时有效",
            example = "urn:smn:cn-east-3:xxx:hertzbeat_test", accessMode = READ_WRITE)
    @Size(max = 300)
    private String smnTopicUrn;

    /**
     * Server酱Token
     */
    @Schema(title = "Server酱Token", description = "通知方式为Server酱时有效",
            example = "SCT193569TSNm6xIabdjqeZPtOGOWcvU1e", accessMode = READ_WRITE)
    @Size(max = 300)
    private String serverChanToken;

    /**
     * Gotify Token
     */
    @Schema(title = "Gotify Token", description = "通知方式为Gotify时有效",
            example = "A845h__ZMqDxZlO", accessMode = READ_WRITE)
    @Size(max = 300)
    private String gotifyToken;

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
    @Schema(title = "创建时间", example = "1612198922000", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 最后修改时间
     */
    @Schema(title = "最后修改时间", example = "1612198444000", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtUpdate;
}
