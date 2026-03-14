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

/**
 * 状态页事件内容实体类
 * <p>
 * 用于记录事件的处理进展和更新信息
 * <p>
 * 主要功能：
 * <ul>
 *   <li>记录事件的处理进展</li>
 *   <li>维护事件的状态变更历史</li>
 *   <li>提供事件的详细信息说明</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_status_page_incident_content")
@Schema(description = "状态页事件内容实体")
public class StatusPageIncidentContent implements Serializable {

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
	 * 事件ID
	 * <p>
	 * 关联的状态页事件ID
	 */
	@Schema(title = "事件ID", example = "1234")
	@TableField("incident_id")
	private Long incidentId;

	/**
	 * 事件内容消息
	 * <p>
	 * 事件更新的详细说明，描述当前的处理进展或发现的问题
	 */
	@Schema(title = "事件内容消息", example = "我们发现网关连接超时问题")
	@NotBlank
	private String message;

	/**
	 * 事件状态
	 * <p>
	 * 此更新对应的事件状态
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
	 * 事件内容消息时间戳
	 * <p>
	 * 此更新的发布时间（毫秒时间戳）
	 */
	@Schema(title = "事件内容消息时间戳", example = "4248574985744")
	private Long timestamp;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该事件内容的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom")
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该事件内容的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者", example = "tom")
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 事件内容创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000")
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 事件内容最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "1612198444000")
	private LocalDateTime gmtUpdate;

}
