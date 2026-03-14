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
 * 状态页历史实体类
 * <p>
 * 用于记录状态页组件的历史状态信息，用于计算可用性和生成统计报表
 * <p>
 * 主要功能：
 * <ul>
 *   <li>记录组件的历史状态变化</li>
 *   <li>统计各状态的持续时间</li>
 *   <li>计算组件的可用性指标</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_status_page_history")
@Schema(description = "状态页组件历史实体")
public class StatusPageHistory implements Serializable {

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
	 * 组件ID
	 * <p>
	 * 关联的状态页组件ID
	 */
	@Schema(title = "组件ID", example = "1234")
	private Long componentId;

	/**
	 * 组件状态
	 * <p>
	 * 历史记录的状态值
	 * <ul>
	 *   <li>0 - 正常</li>
	 *   <li>1 - 异常</li>
	 *   <li>2 - 未知</li>
	 * </ul>
	 */
	@Schema(title = "组件状态: 0-正常 1-异常 2-未知", example = "0")
	private Byte state;

	/**
	 * 状态计算时间戳
	 * <p>
	 * 计算状态的时间点（毫秒时间戳）
	 */
	@Schema(title = "状态计算时间戳", example = "4248574985744")
	private Long timestamp;

	/**
	 * 可用性百分比
	 * <p>
	 * 组件的可用性百分比，范围0-100
	 */
	@Schema(title = "可用性百分比", example = "99.99")
	private Double uptime;

	/**
	 * 异常时间
	 * <p>
	 * 组件处于异常状态的累计时间（秒）
	 */
	@Schema(title = "异常时间(秒)", example = "1000")
	private Integer abnormal;

	/**
	 * 未知时间
	 * <p>
	 * 组件处于未知状态的累计时间（秒）
	 */
	@Schema(title = "未知时间(秒)", example = "1000")
	private Integer unknowing;

	/**
	 * 正常时间
	 * <p>
	 * 组件处于正常状态的累计时间（秒）
	 */
	@Schema(title = "正常时间(秒)", example = "1000")
	private Integer normal;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该历史记录的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom")
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该历史记录的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者", example = "tom")
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 历史记录创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000")
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 历史记录最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "1612198444000")
	private LocalDateTime gmtUpdate;

}
