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
import com.pig4cloud.pig.common.core.entity.manager.Collector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 采集器 Mapper 接口
 * <p>
 * 负责采集器数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>采集器的基础增删改查操作</li>
 *   <li>根据名称查询采集器</li>
 *   <li>批量查询采集器</li>
 *   <li>根据名称删除采集器</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see Collector 采集器实体类
 */
@Mapper
public interface CollectorMapper extends BaseMapper<Collector> {

	/**
	 * 根据名称查询采集器
	 * <p>
	 * 通过采集器的唯一名称查询对应的采集器详情
	 *
	 * @param name 采集器名称
	 * @return 采集器对象，如果不存在则返回null
	 */
	Collector findCollectorByName(@Param("name") String name);

	/**
	 * 根据名称集合批量查询采集器
	 * <p>
	 * 根据提供的采集器名称集合，批量查询对应的采集器详情
	 *
	 * @param names 采集器名称列表
	 * @return 采集器列表
	 */
	List<Collector> findCollectorsByNameIn(@Param("names") List<String> names);

	/**
	 * 根据名称删除采集器
	 * <p>
	 * 通过采集器的唯一名称删除对应的采集器
	 *
	 * @param collector 采集器名称
	 */
	void deleteCollectorByName(@Param("collector") String collector);

}
