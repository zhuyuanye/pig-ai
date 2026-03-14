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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 插件元数据实体类
 * <p>
 * 用于表示系统中的插件元数据信息，包含插件的基本配置和状态
 * <p>
 * 主要功能：
 * <ul>
 *   <li>管理插件的基本信息和配置</li>
 *   <li>控制插件的启用和禁用状态</li>
 *   <li>维护插件文件路径和参数数量</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_plugin_metadata")
@Schema(description = "插件元数据实体")
public class PluginMetadata implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 * <p>
	 * 使用数据库自增策略生成的唯一标识
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(title = "插件主键索引ID", example = "87584674384", accessMode = READ_ONLY)
	private Long id;

	/**
	 * 插件名称
	 * <p>
	 * 插件的显示名称，用于标识和区分不同的插件
	 */
	@Schema(title = "插件名称", example = "通知插件", accessMode = READ_WRITE)
	@NotNull
	private String name;

	/**
	 * 插件激活状态
	 * <p>
	 * 控制插件是否启用的标志
	 * <ul>
	 *   <li>true - 插件已启用，可以正常使用</li>
	 *   <li>false - 插件已禁用，不会加载和执行</li>
	 * </ul>
	 */
	@Schema(title = "插件激活状态", example = "true", accessMode = READ_WRITE)
	private Boolean enableStatus;

	/**
	 * Jar文件路径
	 * <p>
	 * 插件JAR包的存储路径，用于插件加载
	 */
	@Schema(title = "Jar文件路径", example = "/opt/plugins/notification-plugin.jar", accessMode = READ_WRITE)
	private String jarFilePath;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该插件的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom", accessMode = READ_ONLY)
	private String creator;

	/**
	 * 创建时间
	 * <p>
	 * 插件注册的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000", accessMode = READ_ONLY)
	private LocalDateTime gmtCreate;

	/**
	 * 参数数量
	 * <p>
	 * 插件所需的参数配置数量
	 */
	@Schema(title = "参数数量", example = "3", accessMode = READ_WRITE)
	private Integer paramCount;

	/**
	 * 插件项列表
	 * <p>
	 * 该插件包含的所有插件项，需要在Service层通过metadataId查询获取
	 * <p>
	 * 注意：此字段不直接映射到数据库，需要通过PluginItemMapper查询
	 */
	@TableField(exist = false)
	@Schema(title = "插件项列表", accessMode = READ_ONLY)
	private List<PluginItem> items;

	/**
	 * 判断插件元数据是否相等
	 * <p>
	 * 两个插件元数据相等的条件是ID、名称、启用状态、JAR文件路径、创建者和创建时间都相同
	 *
	 * @param o 比较对象
	 * @return 如果所有关键字段都相同则返回true
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PluginMetadata that = (PluginMetadata) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name)
				&& Objects.equals(enableStatus, that.enableStatus) && Objects.equals(jarFilePath, that.jarFilePath)
				&& Objects.equals(creator, that.creator) && Objects.equals(gmtCreate, that.gmtCreate);
	}

	/**
	 * 计算插件元数据的哈希值
	 * <p>
	 * 基于ID、名称、启用状态、JAR文件路径、创建者和创建时间计算哈希值
	 *
	 * @return 哈希值
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, name, enableStatus, jarFilePath, creator, gmtCreate);
	}

}
