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
import com.pig4cloud.pig.common.core.entity.manager.PluginItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 插件项 Mapper 接口
 * <p>
 * 负责插件项数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>插件项的基础增删改查操作</li>
 *   <li>根据类标识符列表统计插件项数量</li>
 *   <li>管理监控插件的定义和配置项</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see PluginItem 插件项实体类
 */
@Mapper
public interface PluginItemMapper extends BaseMapper<PluginItem> {

	/**
	 * 根据类标识符列表统计插件项数量
	 * <p>
	 * 统计指定类标识符列表中已存在的插件项数量，用于插件注册时的重复检查
	 *
	 * @param classIdentifiers 类标识符列表
	 * @return 插件项数量
	 */
	int countPluginItemByClassIdentifierIn(@Param("classIdentifiers") List<String> classIdentifiers);

}
