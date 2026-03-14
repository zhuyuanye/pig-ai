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
import com.pig4cloud.pig.common.core.entity.manager.StatusPageIncidentComponentBind;
import org.apache.ibatis.annotations.Mapper;

/**
 * 状态页事件组件绑定 Mapper 接口
 * <p>
 * 负责状态页事件与组件绑定关系数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>事件组件绑定关系的基础增删改查操作</li>
 *   <li>根据组件ID统计绑定数量</li>
 *   <li>管理故障事件与受影响组件的关联关系</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see StatusPageIncidentComponentBind 状态页事件组件绑定实体类
 */
@Mapper
public interface StatusPageIncidentComponentBindMapper extends BaseMapper<StatusPageIncidentComponentBind> {

	/**
	 * 根据组件ID统计绑定数量
	 * <p>
	 * 统计指定组件关联的事件数量，用于分析组件的故障频率
	 *
	 * @param componentId 组件ID
	 * @return 绑定数量
	 */
	long countByComponentId(long componentId);

}
