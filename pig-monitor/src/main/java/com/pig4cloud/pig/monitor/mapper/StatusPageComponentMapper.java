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
import com.pig4cloud.pig.common.core.entity.manager.StatusPageComponent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 状态页组件 Mapper 接口
 * <p>
 * 负责状态页组件数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>状态页组件的基础增删改查操作</li>
 *   <li>根据组织ID查询组件列表</li>
 *   <li>管理状态展示页面的组件配置</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see StatusPageComponent 状态页组件实体类
 */
@Mapper
public interface StatusPageComponentMapper extends BaseMapper<StatusPageComponent> {

	/**
	 * 根据组织ID查询组件列表
	 * <p>
	 * 查询指定组织下的所有状态页组件，用于构建状态展示页面
	 *
	 * @param orgId 组织ID
	 * @return 组件列表
	 */
	List<StatusPageComponent> findByOrgId(@Param("orgId") long orgId);

}
