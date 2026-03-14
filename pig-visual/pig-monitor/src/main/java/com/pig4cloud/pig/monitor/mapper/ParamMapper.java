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
import com.pig4cloud.pig.common.core.entity.manager.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 监控参数 Mapper 接口
 * <p>
 * 负责监控参数数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>监控参数的基础增删改查操作</li>
 *   <li>根据监控任务ID查询参数列表</li>
 *   <li>根据监控任务ID删除参数</li>
 *   <li>批量删除监控任务参数</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see Param 监控参数实体类
 */
@Mapper
public interface ParamMapper extends BaseMapper<Param> {

	/**
	 * 根据监控任务ID查询参数列表
	 * <p>
	 * 查询指定监控任务的所有参数配置
	 *
	 * @param monitorId 监控任务ID
	 * @return 参数列表
	 */
	List<Param> findParamsByMonitorId(@Param("monitorId") Long monitorId);

	/**
	 * 根据监控任务ID删除参数
	 * <p>
	 * 删除指定监控任务的所有参数配置
	 *
	 * @param monitorId 监控任务ID
	 */
	void deleteParamsByMonitorId(@Param("monitorId") long monitorId);

	/**
	 * 根据监控任务ID集合批量删除参数
	 * <p>
	 * 批量删除多个监控任务的所有参数配置，通常用于批量删除监控任务时清理关联参数
	 *
	 * @param monitorIds 监控任务ID集合
	 */
	void deleteParamsByMonitorIdIn(@Param("monitorIds") Set<Long> monitorIds);

}
