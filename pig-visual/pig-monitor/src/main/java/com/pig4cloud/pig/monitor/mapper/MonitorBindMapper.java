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
import com.pig4cloud.pig.common.core.entity.manager.MonitorBind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 监控绑定 Mapper 接口
 * <p>
 * 负责监控绑定关系数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>监控绑定关系的基础增删改查操作</li>
 *   <li>根据业务ID查询绑定关系</li>
 *   <li>根据监控任务ID删除绑定关系</li>
 *   <li>删除指定的绑定关系</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see MonitorBind 监控绑定实体类
 */
@Mapper
public interface MonitorBindMapper extends BaseMapper<MonitorBind> {

	/**
	 * 根据业务ID查询绑定关系列表
	 * <p>
	 * 查询指定业务对象（如采集器）的所有监控绑定关系
	 *
	 * @param bizId 业务ID
	 * @return 绑定关系列表
	 */
	List<MonitorBind> findMonitorBindsByBizId(@Param("bizId") Long bizId);

	/**
	 * 根据业务ID集合批量查询绑定关系
	 * <p>
	 * 批量查询多个业务对象的监控绑定关系
	 *
	 * @param bizIds 业务ID集合
	 * @return 绑定关系列表
	 */
	List<MonitorBind> findMonitorBindsByBizIdIn(@Param("bizIds") Set<Long> bizIds);

	/**
	 * 根据监控任务ID删除绑定关系
	 * <p>
	 * 删除指定监控任务的所有绑定关系
	 *
	 * @param monitorId 监控任务ID
	 */
	void deleteByMonitorId(@Param("monitorId") Long monitorId);

	/**
	 * 根据业务ID和监控任务ID删除绑定关系
	 * <p>
	 * 删除指定的业务对象与监控任务的绑定关系
	 *
	 * @param bizId     业务ID
	 * @param monitorId 监控任务ID
	 */
	void deleteMonitorBindByBizIdAndMonitorId(@Param("bizId") Long bizId, @Param("monitorId") Long monitorId);

	/**
	 * 根据业务ID集合批量删除绑定关系
	 * <p>
	 * 批量删除多个业务对象的所有监控绑定关系
	 *
	 * @param bizIds 业务ID集合
	 */
	void deleteMonitorBindByBizIdIn(@Param("bizIds") Set<Long> bizIds);

}
