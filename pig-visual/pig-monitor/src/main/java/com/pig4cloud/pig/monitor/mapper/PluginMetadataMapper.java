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

package com.pig4cloud.pig.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.manager.PluginMetadata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 插件元数据 Mapper 接口
 * <p>
 * 负责插件元数据数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>插件元数据的基础增删改查操作</li>
 *   <li>根据名称统计插件元数据数量</li>
 *   <li>查询所有启用的插件</li>
 *   <li>管理监控插件的注册和配置信息</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see PluginMetadata 插件元数据实体类
 */
@Mapper
public interface PluginMetadataMapper extends BaseMapper<PluginMetadata> {

	/**
	 * 根据名称统计插件元数据数量
	 * <p>
	 * 统计指定名称的插件数量，用于验证插件名称是否已存在
	 *
	 * @param name 插件名称
	 * @return 插件数量
	 */
	int countPluginMetadataByName(@Param("name") String name);

	/**
	 * 查询所有启用的插件
	 * <p>
	 * 查询系统中所有启用状态的插件元数据，用于加载可用的监控插件
	 *
	 * @return 启用的插件列表
	 */
	List<PluginMetadata> findPluginMetadataByEnableStatusTrue();

}
