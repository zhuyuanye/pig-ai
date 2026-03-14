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

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 监控定义实体类
 * <p>
 * 用于存储监控类型的定义信息，定义了各种监控类型的配置和参数规范
 * <p>
 * 主要功能：
 * <ul>
 *   <li>存储监控类型的YAML配置定义</li>
 *   <li>定义监控类型的参数结构和验证规则</li>
 *   <li>支持不同监控类型的扩展配置</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_define")
@Schema(description = "监控定义实体")
public class Define implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 监控类型
	 * <p>
	 * 监控类型的唯一标识，也是主键
	 * <p>
	 * 常见的监控类型包括：
	 * <ul>
	 *   <li>http - HTTP监控</li>
	 *   <li>ping - PING监控</li>
	 *   <li>ssh - SSH监控</li>
	 *   <li>mysql - MySQL数据库监控</li>
	 *   <li>redis - Redis监控</li>
	 *   <li>jvm - JVM监控</li>
	 *   <li>tomcat - Tomcat监控</li>
	 *   <li>websocket - WebSocket监控</li>
	 * </ul>
	 */
	@TableId(value = "app", type = IdType.INPUT)
	@Schema(title = "监控类型", example = "websocket")
	private String app;

	/**
	 * 定义内容
	 * <p>
	 * 监控类型的YAML格式配置定义，包含参数结构、默认值、验证规则等信息
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(title = "定义内容", description = "定义YAML配置内容")
	private String content;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该定义的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom")
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该定义的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者")
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 定义创建的时间戳（毫秒）
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间（毫秒时间戳）")
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 定义最后修改的时间戳（毫秒）
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "最后修改时间（毫秒时间戳）")
	private LocalDateTime gmtUpdate;

}
