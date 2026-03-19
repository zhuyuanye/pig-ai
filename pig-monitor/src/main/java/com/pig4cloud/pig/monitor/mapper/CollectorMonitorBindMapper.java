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
import com.pig4cloud.pig.common.core.entity.manager.CollectorMonitorBind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采集器监控绑定 Mapper 接口
 * <p>
 * 负责采集器与监控任务绑定关系数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>采集器监控绑定关系的基础增删改查操作</li>
 *   <li>管理采集器与监控任务的关联关系</li>
 *   <li>支持监控任务在多个采集器之间的分配和调度</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see CollectorMonitorBind 采集器监控绑定实体类
 */
@Mapper
public interface CollectorMonitorBindMapper extends BaseMapper<CollectorMonitorBind> {

	/**
	 * 根据采集器名称查询绑定关系
	 * <p>
	 * 查询指定采集器的所有监控绑定关系
	 *
	 * @param collector 采集器名称
	 * @return 绑定关系列表
	 */
	List<CollectorMonitorBind> findCollectorMonitorBindsByCollector(@Param("collector") String collector);

}
