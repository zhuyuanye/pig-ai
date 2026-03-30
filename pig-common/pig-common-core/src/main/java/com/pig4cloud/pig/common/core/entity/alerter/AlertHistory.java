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

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 * 报警历史记录实体
 * <p>
 * 存储报警的历史数据，包括报警组件、地址、级别、恢复状态等信息。
 *
 * @author pig4cloud
 */
@TableName("hzb_alert_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报警历史记录实体")
public class AlertHistory {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "主键ID", accessMode = READ_ONLY)
    private Long id;

    /**
     * 报警组件名称，例如"CPU监控"
     */
    @Schema(title = "报警名称", example = "CPU监控")
    private String componentName;

    /**
     * 监控组件实例名称
     */
    @Schema(title = "监控组件名称", example = "CPU监控")
    private String instanceName;

    /**
     * 报警地址，例如"192.168.1.1"
     */
    @Schema(title = "报警地址", example = "127.0.0.1")
    private String ipAddress;

    /**
     * 报警所属监控ID
     */
    @Schema(title = "报警所属监控ID", example = "1001")
    private Long monitorId;

    /**
     * 报警指纹ID
     */
    @Schema(title = "报警指纹ID", example = "id")
    private String fingerprintId;

    /**
     * 报警级别，例如"critical"
     */
    @Schema(title = "报警级别", example = "critical")
    private String severity;

    /**
     * 是否恢复，0表示未恢复，1表示已恢复
     */
    @Schema(title = "是否恢复（0未恢复，1已恢复）", example = "1")
    private Integer isResolved;

    /**
     * 报警恢复时间
     */
    @Schema(title = "报警恢复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resolvedTime;

    /**
     * 报警描述，详细说明报警内容
     */
    @Schema(title = "报警描述", example = "CPU使用率超过90%")
    private String description;

    /**
     * 报警时间（记录创建时间）
     */
    @Schema(title = "记录创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAlertTime;
}
