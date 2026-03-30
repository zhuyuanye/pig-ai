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
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 状态页组件实体类
 * <p>
 * 用于表示状态页中的组件信息，组件是状态页的基本单元
 * <p>
 * 主要功能：
 * <ul>
 *   <li>定义状态页的组件信息</li>
 *   <li>维护组件的状态和监控方式</li>
 *   <li>支持组件标签和描述信息</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_status_page_component")
@Schema(description = "状态页组件实体")
public class StatusPageComponent implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 * <p>
	 * 使用数据库自增策略生成的唯一标识
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(title = "主键ID", example = "87584674384")
	private Long id;

	/**
	 * 组织ID
	 * <p>
	 * 关联的状态页组织ID
	 */
	@Schema(title = "组织ID", example = "1234")
	private Long orgId;

	/**
	 * 组件名称
	 * <p>
	 * 组件的显示名称，如"Gateway"、"Database"等
	 */
	@Schema(title = "组件名称", example = "Gateway")
	@NotBlank
	private String name;

	/**
	 * 组件描述
	 * <p>
	 * 对组件功能的详细说明
	 */
	@Schema(title = "组件描述", example = "网关服务")
	private String description;

	/**
	 * 组件标签
	 * <p>
	 * 用于过滤和分类组件的标签信息，以JSON格式存储
	 */
	@TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
	@Schema(title = "组件标签", example = "{env:test}", accessMode = READ_WRITE)
	private Map<String, String> labels;

	/**
	 * 状态计算方式
	 * <p>
	 * 可选值：
	 * <ul>
	 *   <li>0 - 自动计算，根据关联监控任务的状态自动计算</li>
	 *   <li>1 - 手动配置，由用户手动配置状态</li>
	 * </ul>
	 */
	@Schema(title = "状态计算方式: 0-自动 1-手动", example = "0")
	private Byte method;

	/**
	 * 配置状态
	 * <p>
	 * 当使用手动方式时，配置的状态值
	 * <ul>
	 *   <li>0 - 正常</li>
	 *   <li>1 - 异常</li>
	 *   <li>2 - 未知</li>
	 * </ul>
	 */
	@Schema(title = "配置状态: 0-正常 1-异常 2-未知", example = "0")
	private Byte configState;

	/**
	 * 组件当前状态
	 * <p>
	 * 组件的实际运行状态
	 * <ul>
	 *   <li>0 - 正常</li>
	 *   <li>1 - 异常</li>
	 *   <li>2 - 未知</li>
	 * </ul>
	 */
	@Schema(title = "组件当前状态: 0-正常 1-异常 2-未知", example = "0")
	private Byte state;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该组件的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom")
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该组件的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者", example = "tom")
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 组件创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000")
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 组件最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "1612198444000")
	private LocalDateTime gmtUpdate;

}
