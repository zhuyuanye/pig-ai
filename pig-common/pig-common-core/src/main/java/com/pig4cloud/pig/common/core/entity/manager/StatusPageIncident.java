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
import java.util.List;
import java.util.Set;

/**
 * 状态页事件实体类
 * <p>
 * 用于表示状态页中的事件信息，记录系统故障和处理过程
 * <p>
 * 主要功能：
 * <ul>
 *   <li>记录系统故障事件</li>
 *   <li>跟踪事件处理进度</li>
 *   <li>关联受影响的组件</li>
 *   <li>维护事件更新内容</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_status_page_incident")
@Schema(description = "状态页事件实体")
public class StatusPageIncident implements Serializable {

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
	 * 事件名称
	 * <p>
	 * 事件的显示名称，简明扼要地描述问题
	 */
	@Schema(title = "事件名称", example = "网关连接超时")
	@NotBlank
	private String name;

	/**
	 * 事件当前状态
	 * <p>
	 * 表示事件当前的处理阶段
	 * <ul>
	 *   <li>0 - 调查中 (Investigating)</li>
	 *   <li>1 - 已识别 (Identified)</li>
	 *   <li>2 - 监控中 (Monitoring)</li>
	 *   <li>3 - 已解决 (Resolved)</li>
	 * </ul>
	 */
	@Schema(title = "事件状态: 0-调查中 1-已识别 2-监控中 3-已解决", example = "0")
	private Byte state;

	/**
	 * 事件开始时间
	 * <p>
	 * 事件开始调查的时间戳（毫秒）
	 */
	@Schema(title = "事件开始时间", example = "4248574985744")
	private Long startTime;

	/**
	 * 事件结束时间
	 * <p>
	 * 事件已解决的时间戳（毫秒）
	 */
	@Schema(title = "事件结束时间", example = "4248574985744")
	private Long endTime;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该事件的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom")
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该事件的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者", example = "tom")
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 事件创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000")
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 事件最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "1612198444000")
	private LocalDateTime gmtUpdate;

	/**
	 * 受影响的组件列表
	 * <p>
	 * 该事件影响的所有组件，需要在Service层通过StatusPageIncidentComponentBind查询获取
	 * <p>
	 * 注意：此字段不直接映射到数据库，需要通过Mapper查询
	 */
	@TableField(exist = false)
	@Schema(title = "受影响的组件列表")
	private List<StatusPageComponent> components;

	/**
	 * 事件更新内容列表
	 * <p>
	 * 该事件的所有更新记录，需要在Service层通过incidentId查询获取
	 * <p>
	 * 注意：此字段不直接映射到数据库，需要通过Mapper查询
	 */
	@TableField(exist = false)
	@Schema(title = "事件更新内容列表")
	private Set<StatusPageIncidentContent> contents;

}
