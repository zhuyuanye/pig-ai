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

package com.pig4cloud.pig.common.core.entity.manager;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * Common Config Entity
 */
@TableName("hzb_config")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Common server config entity")
public class GeneralConfig {

    @TableId
    @Schema(title = "Config type: email sms, primary key ", description = "Config type: email sms, primary key ",
            accessMode = READ_WRITE)
    @NotBlank(message = "ip can not null")
    private String type;

    @Schema(title = "Config content", description = "Config content，formatjson", accessMode = READ_WRITE)
    private String content;

    @Schema(title = "The creator of this record", example = "tom", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @Schema(title = "This record was last modified by", example = "tom", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifier;

    @Schema(title = "This record creation time (millisecond timestamp)", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @Schema(title = "Record the latest modification time (timestamp in milliseconds)", accessMode = READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtUpdate;
}
