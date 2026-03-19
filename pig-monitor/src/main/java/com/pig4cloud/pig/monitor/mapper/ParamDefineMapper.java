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
import com.pig4cloud.pig.common.core.entity.manager.ParamDefine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 参数定义 Mapper 接口
 * <p>
 * 负责参数定义数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>参数定义的基础增删改查操作</li>
 *   <li>根据监控类型查询参数定义列表</li>
 *   <li>管理监控指标的参数定义和配置</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see ParamDefine 参数定义实体类
 */
@Mapper
public interface ParamDefineMapper extends BaseMapper<ParamDefine> {

	/**
	 * 根据监控类型查询参数定义列表
	 * <p>
	 * 查询指定监控类型（如http、ping、ssh等）的所有参数定义
	 * <p>
	 * 常用监控类型：
	 * <ul>
	 *   <li>http - HTTP监控参数</li>
	 *   <li>ping - PING监控参数</li>
	 *   <li>ssh - SSH监控参数</li>
	 *   <li>mysql - MySQL数据库监控参数</li>
	 *   <li>redis - Redis监控参数</li>
	 * </ul>
	 *
	 * @param app 监控类型
	 * @return 参数定义列表
	 */
	List<ParamDefine> findParamDefinesByApp(@Param("app") String app);

}
