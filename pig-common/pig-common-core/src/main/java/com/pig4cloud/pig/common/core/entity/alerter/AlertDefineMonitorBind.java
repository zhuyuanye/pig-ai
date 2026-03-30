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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.pig4cloud.pig.common.core.entity.manager.Monitor;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 告警规则与监控绑定关系实体
 * <p>
 * 定义告警阈值规则与监控对象之间的关联关系。
 *
 * @author pig4cloud
 */
@TableName("hzb_alert_define_monitor_bind")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警规则与监控绑定关系实体")
public class AlertDefineMonitorBind {

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "主键ID", example = "74384", accessMode = READ_ONLY)
    private Long id;

    /**
     * 告警规则ID
     */
    @Schema(title = "告警规则ID", example = "87432674384", accessMode = READ_WRITE)
    private Long alertDefineId;

    /**
     * 监控ID
     */
    @Schema(title = "监控ID", example = "87432674336", accessMode = READ_WRITE)
    private Long monitorId;

    /**
     * 记录创建时间
     */
    @Schema(title = "记录创建时间", example = "1612198922000", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 记录修改时间
     */
    @Schema(title = "记录修改时间", example = "1612198444000", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtUpdate;

    /**
     * 关联的监控对象（非数据库字段，查询时填充）
     */
    @TableField(exist = false)
    private Monitor monitor;
}
