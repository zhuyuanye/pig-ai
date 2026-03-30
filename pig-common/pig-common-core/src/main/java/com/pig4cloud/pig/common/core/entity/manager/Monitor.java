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
import com.pig4cloud.pig.common.core.support.valid.HostValid;
import com.pig4cloud.pig.common.core.util.JsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 监控任务实体类
 * <p>
 * 用于表示系统中的监控任务，包含监控任务的基本信息、配置参数、状态等
 * <p>
 * 主要功能：
 * <ul>
 *   <li>监控任务的增删改查</li>
 *   <li>监控任务的生命周期管理</li>
 *   <li>监控任务与采集器的绑定管理</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_monitor")
@Schema(description = "监控任务实体")
public class Monitor implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID - 监控任务ID
	 * <p>
	 * 使用雪花算法生成的唯一标识
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@Schema(title = "监控任务ID", example = "87584674384", accessMode = READ_ONLY)
	private Long id;

	/**
	 * 采集任务ID
	 * <p>
	 * 关联到采集器中的具体任务ID
	 */
	@Schema(title = "采集任务ID", example = "43243543543", accessMode = READ_ONLY)
	private Long jobId;

	/**
	 * 任务名称
	 * <p>
	 * 监控任务的显示名称，用于识别和区分不同的监控任务
	 */
	@Schema(title = "任务名称", example = "Api-TanCloud.cn", accessMode = READ_WRITE)
	@Size(max = 100)
	private String name;

	/**
	 * 监控类型
	 * <p>
	 * 可选值：
	 * <ul>
	 *   <li>http - HTTP监控</li>
	 *   <li>ping - PING监控</li>
	 *   <li>ssh - SSH监控</li>
	 *   <li>mysql - MySQL数据库监控</li>
	 *   <li>redis - Redis监控</li>
	 *   <li>其他自定义监控类型</li>
	 * </ul>
	 */
	@Schema(title = "监控类型", example = "TanCloud", accessMode = READ_WRITE)
	@Size(max = 100)
	private String app;

	/**
	 * 采集类型
	 * <p>
	 * 支持的服务发现方式：
	 * <ul>
	 *   <li>static - 静态配置</li>
	 *   <li>http_sd - HTTP服务发现</li>
	 *   <li>dns_sd - DNS服务发现</li>
	 *   <li>zookeeper_sd - Zookeeper服务发现</li>
	 * </ul>
	 */
	@Schema(title = "采集类型", example = "static", accessMode = READ_WRITE)
	@Size(max = 100)
	private String scrape;

	/**
	 * 对端主机
	 * <p>
	 * 监控目标的地址，支持IPv4、IPv6、域名格式
	 */
	@Schema(title = "对端主机", example = "192.167.25.11", accessMode = READ_WRITE)
	@Size(max = 100)
	@HostValid
	private String host;

	/**
	 * 监控采集间隔时间（秒）
	 * <p>
	 * 最小值为10秒，用于控制监控任务的执行频率
	 */
	@Schema(title = "采集间隔（秒）", example = "600", accessMode = READ_WRITE)
	@Min(10)
	private Integer intervals;

	/**
	 * 任务状态
	 * <p>
	 * 可选值：
	 * <ul>
	 *   <li>0 - 暂停</li>
	 *   <li>1 - 正常</li>
	 *   <li>2 - 异常</li>
	 *   <li>3 - 禁用</li>
	 *   <li>4 - 未知</li>
	 * </ul>
	 */
	@Schema(title = "任务状态 0: 暂停, 1: 正常, 2: 异常", accessMode = READ_WRITE)
	@Min(0)
	@Max(4)
	private Byte status;

	/**
	 * 任务类型
	 * <p>
	 * 可选值：
	 * <ul>
	 *   <li>0 - 普通监控，用户手动创建</li>
	 *   <li>1 - 推送自动创建，通过推送接口自动创建</li>
	 *   <li>2 - 发现自动创建，通过服务发现自动创建</li>
	 * </ul>
	 */
	@Schema(title = "任务类型 0: 普通监控, 1: 推送自动创建, 2: 发现自动创建")
	private Byte type;

	/**
	 * 任务标签
	 * <p>
	 * 用于对监控任务进行分类和过滤，以JSON格式存储
	 * <p>
	 * 示例：{"env":"test","team":"ops"}
	 */
	@TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
	@Schema(title = "任务标签", example = "{env:test}", accessMode = READ_WRITE)
	private Map<String, String> labels;

	/**
	 * 任务注解
	 * <p>
	 * 用于存储监控任务的扩展信息和描述，以JSON格式存储
	 * <p>
	 * 示例：{"summary":"this task looks good","priority":"high"}
	 */
	@TableField(typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapTypeHandler.class)
	@Schema(title = "任务注解", example = "{summary:this task looks good}", accessMode = READ_WRITE)
	private Map<String, String> annotations;

	/**
	 * 监控描述
	 * <p>
	 * 对监控任务的详细说明和描述信息
	 */
	@Schema(title = "监控描述", example = "SAAS网站TanCloud的可用性监控", accessMode = READ_WRITE)
	@Size(max = 255)
	private String description;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该监控任务的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "admin", accessMode = READ_ONLY)
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该监控任务的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者", example = "admin", accessMode = READ_ONLY)
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 监控任务创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "2024-07-02T20:09:34.903217", accessMode = READ_ONLY)
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 监控任务最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "2024-07-02T20:09:34.903217", accessMode = READ_ONLY)
	private LocalDateTime gmtUpdate;

	/**
	 * 克隆当前监控任务对象
	 * <p>
	 * 通过JSON序列化和反序列化实现对象的深拷贝
	 *
	 * @return 监控任务的克隆对象
	 */
	@Override
	public Monitor clone() {
		return JsonUtil.fromJson(JsonUtil.toJson(this), getClass());
	}

}
