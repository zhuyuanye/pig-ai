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
 * 采集器监控绑定实体类
 * <p>
 * 用于表示采集器与监控任务的绑定关系
 * <p>
 * 主要功能：
 * <ul>
 *   <li>维护采集器与监控任务的关联关系</li>
 *   <li>支持监控任务在不同采集器之间的分配</li>
 *   <li>实现采集器负载均衡管理</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_collector_monitor_bind")
@Schema(description = "采集器监控绑定实体")
public class CollectorMonitorBind implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 * <p>
	 * 使用数据库自增策略生成的唯一标识
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(title = "主键ID", example = "23")
	private Long id;

	/**
	 * 采集器名称
	 * <p>
	 * 关联的采集器名称，标识负责执行监控任务的采集器
	 */
	@Schema(title = "采集器名称", example = "collector-1")
	private String collector;

	/**
	 * 监控任务ID
	 * <p>
	 * 关联的监控任务ID，表示该采集器负责执行哪个监控任务
	 */
	@Schema(title = "监控任务ID", example = "87432674336")
	@TableField("monitor_id")
	private Long monitorId;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该绑定关系的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom")
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该绑定关系的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者")
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 绑定关系创建的时间戳（毫秒）
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间（毫秒时间戳）")
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 绑定关系最后修改的时间戳（毫秒）
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "最后修改时间（毫秒时间戳）")
	private LocalDateTime gmtUpdate;

}
