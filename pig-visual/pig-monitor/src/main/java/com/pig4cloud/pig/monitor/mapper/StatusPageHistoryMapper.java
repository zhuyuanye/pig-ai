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
import com.pig4cloud.pig.common.core.entity.manager.StatusPageHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 状态页历史 Mapper 接口
 * <p>
 * 负责状态页历史数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>状态页历史数据的基础增删改查操作</li>
 *   <li>根据时间戳范围查询历史记录</li>
 *   <li>根据组件ID和时间戳范围查询历史记录</li>
 *   <li>管理状态页面的历史状态数据</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see StatusPageHistory 状态页历史实体类
 */
@Mapper
public interface StatusPageHistoryMapper extends BaseMapper<StatusPageHistory> {

	/**
	 * 根据时间戳范围查询状态页历史
	 * <p>
	 * 查询指定时间范围内的所有状态页历史记录，用于生成状态趋势图表
	 *
	 * @param start 开始时间戳（毫秒）
	 * @param end   结束时间戳（毫秒）
	 * @return 状态页历史列表
	 */
	List<StatusPageHistory> findStatusPageHistoriesByTimestampBetween(@Param("start") long start,
			@Param("end") long end);

	/**
	 * 根据组件ID和时间戳范围查询状态页历史
	 * <p>
	 * 查询指定组件在特定时间范围内的历史记录，用于展示单个组件的状态变化趋势
	 *
	 * @param componentId 组件ID
	 * @param start       开始时间戳（毫秒）
	 * @param end         结束时间戳（毫秒）
	 * @return 状态页历史列表
	 */
	List<StatusPageHistory> findStatusPageHistoriesByComponentIdAndTimestampBetween(@Param("componentId") long componentId,
			@Param("start") long start, @Param("end") long end);

}
