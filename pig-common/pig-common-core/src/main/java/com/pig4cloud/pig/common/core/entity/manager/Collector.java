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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 采集器实体类
 * <p>
 * 用于表示系统中的采集器信息，采集器负责执行具体的监控任务
 * <p>
 * 主要功能：
 * <ul>
 *   <li>采集器的注册和管理</li>
 *   <li>采集器状态的监控</li>
 *   <li>采集器与监控任务的绑定</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_collector")
@Schema(description = "采集器实体")
public class Collector implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 * <p>
	 * 采集器的唯一标识，使用数据库自增策略
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(title = "主键ID", example = "2")
	private Long id;

	/**
	 * 采集器名称
	 * <p>
	 * 采集器的唯一标识名称，在系统中必须唯一
	 */
	@Schema(title = "采集器名称", description = "采集器的唯一标识名称")
	@NotBlank(message = "name can not null")
	private String name;

	/**
	 * 采集器IP地址
	 * <p>
	 * 采集器所在服务器的IP地址
	 */
	@Schema(title = "采集器IP地址", description = "采集器远程IP地址")
	@NotBlank(message = "ip can not null")
	private String ip;

	/**
	 * 采集器版本
	 * <p>
	 * 采集器程序的版本号
	 */
	@Schema(title = "采集器版本", description = "采集器版本号")
	private String version;

	/**
	 * 采集器状态
	 * <p>
	 * 可选值：
	 * <ul>
	 *   <li>0 - 在线</li>
	 *   <li>1 - 离线</li>
	 * </ul>
	 */
	@Schema(title = "采集器状态: 0-在线 1-离线")
	@Min(0)
	private Byte status;

	/**
	 * 采集器模式
	 * <p>
	 * 可选值：
	 * <ul>
	 *   <li>public - 公共采集器，可供所有监控任务使用</li>
	 *   <li>private - 私有采集器，仅供特定监控任务使用</li>
	 * </ul>
	 */
	@Schema(title = "采集器模式: public(公共) or private(私有)")
	private String mode;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该采集器的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom")
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该采集器的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者")
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 采集器注册的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间（毫秒时间戳）")
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 采集器最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "最后修改时间（毫秒时间戳）")
	private LocalDateTime gmtUpdate;

}
