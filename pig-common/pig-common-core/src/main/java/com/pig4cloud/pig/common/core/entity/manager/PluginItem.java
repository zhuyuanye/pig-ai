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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pig4cloud.pig.common.core.constants.PluginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 插件项实体类
 * <p>
 * 用于表示系统中的具体插件实现，每个插件项对应一个可执行的插件类
 * <p>
 * 主要功能：
 * <ul>
 *   <li>定义插件的具体实现类</li>
 *   <li>标识插件的类型和用途</li>
 *   <li>关联到插件元数据</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_plugin_item")
@Schema(description = "插件项实体")
public class PluginItem implements Serializable {

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
	 * 插件元数据ID
	 * <p>
	 * 关联的插件元数据ID，表示该插件项属于哪个插件
	 * <p>
	 * 注意：此字段不在JSON序列化中显示
	 */
	@TableField("metadata_id")
	@JsonIgnore
	private Long metadataId;

	/**
	 * 插件实现类全路径
	 * <p>
	 * 插件具体实现类的完整类名，用于反射加载和实例化插件
	 * <p>
	 * 示例：com.pig4cloud.pig.common.plugin.impl.DemoPluginImpl
	 */
	@Schema(title = "插件实现类全路径", example = "com.pig4cloud.pig.common.plugin.impl.DemoPluginImpl",
			accessMode = READ_WRITE)
	private String classIdentifier;

	/**
	 * 插件类型
	 * <p>
	 * 定义插件的类型，用于区分不同用途的插件
	 * <p>
	 * 常见的插件类型：
	 * <ul>
	 *   <li>POST_ALERT - 告警后置处理插件</li>
	 *   <li>PRE_ALERT - 告警前置处理插件</li>
	 *   <li>NOTIFICATION - 通知插件</li>
	 *   <li>其他自定义类型</li>
	 * </ul>
	 */
	@Schema(title = "插件类型", example = "POST_ALERT", accessMode = READ_WRITE)
	private String type;

	/**
	 * 构造函数
	 * <p>
	 * 用于快速创建插件项对象
	 *
	 * @param classIdentifier 插件实现类全路径
	 * @param type            插件类型
	 */
	public PluginItem(String classIdentifier, PluginType type) {
		this.classIdentifier = classIdentifier;
		this.type = type.name();
	}

	/**
	 * 判断插件项是否相等
	 * <p>
	 * 两个插件项相等的条件是ID、类标识符和类型都相同
	 *
	 * @param o 比较对象
	 * @return 如果ID、类标识符和类型都相同则返回true
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PluginItem that = (PluginItem) o;
		return Objects.equals(id, that.id) && Objects.equals(classIdentifier, that.classIdentifier)
				&& Objects.equals(type, that.type);
	}

	/**
	 * 计算插件项的哈希值
	 * <p>
	 * 基于ID、类标识符和类型计算哈希值
	 *
	 * @return 哈希值
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, classIdentifier, type);
	}

}
