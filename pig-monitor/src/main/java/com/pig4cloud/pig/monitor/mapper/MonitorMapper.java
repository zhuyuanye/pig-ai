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
import com.pig4cloud.pig.common.core.entity.manager.Monitor;
import com.pig4cloud.pig.monitor.pojo.dto.AppCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 监控任务 Mapper 接口
 * <p>
 * 负责监控任务数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>监控任务的基础增删改查操作</li>
 *   <li>根据ID集合批量删除监控任务</li>
 *   <li>根据监控类型查询监控任务列表</li>
 *   <li>查询已下发采集任务的监控任务</li>
 *   <li>统计各监控类型和状态的数量</li>
 *   <li>更新监控任务状态</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see Monitor 监控任务实体类
 */
@Mapper
public interface MonitorMapper extends BaseMapper<Monitor> {

	/**
	 * 根据ID集合批量删除监控任务
	 * <p>
	 * 用于批量删除指定的监控任务，通常用于用户批量删除操作
	 *
	 * @param monitorIds 监控任务ID集合
	 */
	void deleteAllByIdIn(@Param("monitorIds") Set<Long> monitorIds);

	/**
	 * 根据ID集合查询监控任务列表
	 * <p>
	 * 根据提供的监控任务ID集合，查询对应的监控任务详情
	 *
	 * @param monitorIds 监控任务ID集合
	 * @return 监控任务列表
	 */
	List<Monitor> findMonitorsByIdIn(@Param("monitorIds") Set<Long> monitorIds);

	/**
	 * 根据监控类型查询监控任务列表
	 * <p>
	 * 查询指定监控类型（如http、ping、ssh等）的所有监控任务
	 * <p>
	 * 常用监控类型：
	 * <ul>
	 *   <li>http - HTTP监控</li>
	 *   <li>ping - PING监控</li>
	 *   <li>ssh - SSH监控</li>
	 *   <li>mysql - MySQL数据库监控</li>
	 *   <li>redis - Redis监控</li>
	 * </ul>
	 *
	 * @param app 监控类型
	 * @return 监控任务列表，按创建时间倒序排列
	 */
	List<Monitor> findMonitorsByAppEquals(@Param("app") String app);

	/**
	 * 查询已下发采集任务的监控任务（排除指定状态）
	 * <p>
	 * 查询已经分配采集任务ID（job_id不为null）且状态不在排除列表中的监控任务
	 * <p>
	 * 主要用于：
	 * <ul>
	 *   <li>获取正在运行中的监控任务</li>
	 *   <li>过滤掉暂停或禁用的监控任务</li>
	 *   <li>定时任务扫描需要执行的监控</li>
	 * </ul>
	 *
	 * @param statusList 要排除的状态列表，如[0]表示排除暂停状态的监控
	 * @return 监控任务列表
	 */
	List<Monitor> findMonitorsByStatusNotInAndJobIdNotNull(@Param("statusList") List<Byte> statusList);

	/**
	 * 根据监控名称查询监控任务
	 * <p>
	 * 通过监控任务的唯一名称查询对应的监控任务详情
	 * <p>
	 * 注意：监控名称在系统中应该是唯一的，此方法只返回第一个匹配的结果
	 *
	 * @param name 监控任务名称
	 * @return 监控任务对象，如果不存在则返回null
	 */
	Monitor findMonitorByNameEquals(@Param("name") String name);

	/**
	 * 统计各监控类型和状态的数量
	 * <p>
	 * 按监控类型（app）和状态（status）分组统计监控任务的数量
	 * <p>
	 * 主要用途：
	 * <ul>
	 *   <li>生成监控任务的统计报表</li>
	 *   <li>展示各类型监控的运行状态分布</li>
	 *   <li>仪表板数据展示</li>
	 * </ul>
	 *
	 * @return 监控类型-状态-数量统计列表
	 */
	List<AppCount> findAppsStatusCount();

	/**
	 * 更新监控任务状态
	 * <p>
	 * 更新指定监控任务的状态，并自动更新修改时间
	 * <p>
	 * 状态值：
	 * <ul>
	 *   <li>0 - 暂停</li>
	 *   <li>1 - 正常</li>
	 *   <li>2 - 异常</li>
	 *   <li>3 - 禁用</li>
	 * </ul>
	 *
	 * @param id     监控任务ID
	 * @param status 新的状态值
	 */
	void updateMonitorStatus(@Param("id") Long id, @Param("status") Byte status);

}
