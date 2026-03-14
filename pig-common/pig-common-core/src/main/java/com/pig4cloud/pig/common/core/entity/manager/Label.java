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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 标签实体类
 * <p>
 * 用于表示系统中的标签信息，标签可用于对监控任务进行分类和组织
 * <p>
 * 主要功能：
 * <ul>
 *   <li>标签的创建和管理</li>
 *   <li>标签与监控任务的关联</li>
 *   <li>支持自动生成、用户创建和系统预设三种类型</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_tag")
@Schema(description = "标签实体")
public class Label implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 * <p>
	 * 使用数据库自增策略生成的唯一标识
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(title = "主键索引ID", example = "87584674384", accessMode = READ_ONLY)
	private Long id;

	/**
	 * 标签名称
	 * <p>
	 * 标签的字段名，如"app"、"env"等
	 */
	@Schema(title = "标签字段名", example = "app", accessMode = READ_WRITE)
	@NotBlank(message = "name can not null")
	private String name;

	/**
	 * 标签值
	 * <p>
	 * 标签的具体值，支持最大2048字符
	 */
	@Schema(title = "标签值", example = "23", accessMode = READ_WRITE)
	private String tagValue;

	/**
	 * 标签描述
	 * <p>
	 * 对标签用途和含义的详细说明
	 */
	@Schema(title = "标签描述", example = "用于监控MySQL", accessMode = READ_WRITE)
	private String description;

	/**
	 * 标签类型
	 * <p>
	 * 可选值：
	 * <ul>
	 *   <li>0 - 自动生成，系统根据监控任务自动创建</li>
	 *   <li>1 - 用户创建，用户手动创建的标签</li>
	 *   <li>2 - 系统预设，系统预定义的标签</li>
	 *   <li>3 - 其他类型</li>
	 * </ul>
	 */
	@Schema(title = "标签类型 0:自动生成 1:用户创建 2:系统预设", accessMode = READ_WRITE)
	@Min(0)
	@Max(3)
	private Byte type;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该标签的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom", accessMode = READ_ONLY)
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该标签的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者", example = "tom", accessMode = READ_ONLY)
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 标签创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000", accessMode = READ_ONLY)
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 标签最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "1612198444000", accessMode = READ_ONLY)
	private LocalDateTime gmtUpdate;

	/**
	 * 判断标签是否相等
	 * <p>
	 * 两个标签相等的条件是名称和值都相同
	 *
	 * @param o 比较对象
	 * @return 如果名称和值都相同则返回true
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Label tag = (Label) o;
		return Objects.equals(name, tag.name) && Objects.equals(tagValue, tag.tagValue);
	}

	/**
	 * 计算标签的哈希值
	 * <p>
	 * 基于标签名称和值计算哈希值
	 *
	 * @return 哈希值
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 13 * hash + (name == null ? 0 : name.hashCode()) + (tagValue == null ? 0 : tagValue.hashCode());
		return hash;
	}

}
