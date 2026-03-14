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
import com.pig4cloud.pig.common.core.util.JsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 监控参数实体类
 * <p>
 * 用于存储监控任务的参数配置信息，每个监控任务可以有多个参数
 * <p>
 * 主要功能：
 * <ul>
 *   <li>存储监控任务的配置参数</li>
 *   <li>支持多种参数类型（数字、字符串、加密字符串、JSON等）</li>
 *   <li>参数与监控任务的关联管理</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_param")
@Schema(description = "监控参数实体")
public class Param implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 参数主键ID
	 * <p>
	 * 使用数据库自增策略生成的唯一标识
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(title = "参数主键ID", example = "87584674384", accessMode = READ_ONLY)
	private Long id;

	/**
	 * 监控任务ID
	 * <p>
	 * 关联的监控任务ID，表示该参数属于哪个监控任务
	 */
	@Schema(title = "监控任务ID", example = "875846754543", accessMode = READ_WRITE)
	private Long monitorId;

	/**
	 * 参数字段标识
	 * <p>
	 * 参数的唯一标识符，用于区分不同的参数
	 * <p>
	 * 示例：
	 * <ul>
	 *   <li>port - 端口号</li>
	 *   <li>url - 请求地址</li>
	 *   <li>username - 用户名</li>
	 *   <li>password - 密码</li>
	 * </ul>
	 */
	@Schema(title = "参数字段标识", example = "port", accessMode = READ_WRITE)
	@Size(max = 100)
	@NotBlank(message = "field can not null")
	private String field;

	/**
	 * 参数值
	 * <p>
	 * 参数的具体值，根据参数类型存储不同格式的数据
	 */
	@Schema(title = "参数值", example = "8080", accessMode = READ_WRITE)
	@Size(max = 8126)
	private String paramValue;

	/**
	 * 参数类型
	 * <p>
	 * 可选值：
	 * <ul>
	 *   <li>0 - 数字类型</li>
	 *   <li>1 - 字符串类型</li>
	 *   <li>2 - 加密字符串类型（如密码）</li>
	 *   <li>3 - JSON映射字符串（Map格式）</li>
	 *   <li>4 - 字符串数组</li>
	 * </ul>
	 */
	@Schema(title = "参数类型 0:数字 1:字符串 2:加密字符串 3:Map格式JSON字符串 4:字符串数组", accessMode = READ_WRITE)
	@Min(0)
	private Byte type;

	/**
	 * 创建时间
	 * <p>
	 * 参数创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000", accessMode = READ_ONLY)
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 参数最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "1612198444000", accessMode = READ_ONLY)
	private LocalDateTime gmtUpdate;

	/**
	 * 克隆当前参数对象
	 * <p>
	 * 通过JSON序列化和反序列化实现对象的深拷贝
	 *
	 * @return 参数的克隆对象
	 */
	@Override
	public Param clone() {
		// 深拷贝
		return JsonUtil.fromJson(JsonUtil.toJson(this), getClass());
	}

}
