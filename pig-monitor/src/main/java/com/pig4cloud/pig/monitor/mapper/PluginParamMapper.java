/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work.  Additional information regarding copyright ownership.
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
import com.pig4cloud.pig.monitor.pojo.dto.PluginParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 插件参数 Mapper 接口
 * <p>
 * 负责插件参数数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>插件参数的基础增删改查操作</li>
 *   <li>根据插件元数据ID查询关联的参数列表</li>
 *   <li>根据插件元数据ID批量删除参数</li>
 *   <li>管理插件的配置参数信息</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see PluginParam 插件参数数据传输对象
 */
@Mapper
public interface PluginParamMapper extends BaseMapper<PluginParam> {

	/**
	 * 根据插件元数据ID查询关联的参数列表
	 * <p>
	 * 查询指定插件元数据关联的所有参数信息，用于获取插件的完整配置参数
	 *
	 * @param pluginMetadataId 插件元数据ID
	 * @return 参数值列表
	 */
	List<PluginParam> findParamsByPluginMetadataId(@Param("pluginMetadataId") Long pluginMetadataId);

	/**
	 * 根据插件元数据ID删除关联的参数列表
	 * <p>
	 * 删除指定插件元数据关联的所有参数，通常用于插件删除或参数重置场景
	 *
	 * @param pluginMetadataId 插件元数据ID
	 */
	void deletePluginParamsByPluginMetadataId(@Param("pluginMetadataId") long pluginMetadataId);

	/**
	 * 根据插件元数据ID列表批量删除关联的参数
	 * <p>
	 * 批量删除多个插件元数据关联的所有参数，用于批量删除插件的场景
	 *
	 * @param pluginMetadataIds 插件元数据ID集合
	 */
	void deletePluginParamsByPluginMetadataIdIn(@Param("pluginMetadataIds") Set<Long> pluginMetadataIds);

}
