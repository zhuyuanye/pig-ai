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
 * 状态页事件组件绑定实体类
 * <p>
 * 用于表示事件与组件之间的关联关系
 * <p>
 * 主要功能：
 * <ul>
 *   <li>维护事件与组件的多对多关系</li>
 *   <li>标识哪些组件受到事件影响</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_status_page_incident_component_bind")
@Schema(description = "事件组件绑定关系")
public class StatusPageIncidentComponentBind implements Serializable {

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
	@Schema(title = "事件ID", example = "87432674384")
	@TableField("incident_id")
	private Long incidentId;

	/**
	 * 组件ID
	 * <p>
	 * 关联的状态页组件ID
	 */
	@Schema(title = "组件ID", example = "87432674336")
	@TableField("component_id")
	private Long componentId;

	/**
	 * 创建时间
	 * <p>
	 * 绑定关系创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000")
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 绑定关系最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "1612198444000")
	private LocalDateTime gmtUpdate;

}
