/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work.  Additional information regarding copyright ownership.
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
import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SNMP配置 Mapper 接口
 * <p>
 * 负责SNMP（简单网络管理协议）配置的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>SNMP配置的基础增删改查操作</li>
 *   <li>根据运行状态类型查询SNMP配置列表</li>
 *   <li>管理网络设备的SNMP监控配置</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see SnmpConfigInfo SNMP配置信息实体类
 */
@Mapper
public interface SnmpConfigMapper extends BaseMapper<SnmpConfigInfo> {

	/**
	 * 查询指定运行状态的SNMP配置列表
	 * <p>
	 * 根据运行状态类型（运行中/已停止）查询SNMP配置
	 * <p>
	 * 运行状态类型：
	 * <ul>
	 *   <li>1 - 运行中</li>
	 *   <li>0 - 已停止</li>
	 * </ul>
	 *
	 * @param isRunType 运行状态类型
	 * @return SNMP配置列表
	 */
	List<SnmpConfigInfo> findByIsRunType(@Param("isRunType") String isRunType);

}
