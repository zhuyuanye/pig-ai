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
import com.pig4cloud.pig.common.core.entity.network.NetworkTopologyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网络拓扑数据 Mapper 接口
 * <p>
 * 负责网络拓扑数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>网络拓扑数据的基础增删改查操作</li>
 *   <li>根据配置ID查询网络拓扑信息</li>
 *   <li>根据MAC地址查询网络拓扑信息</li>
 *   <li>根据配置ID批量删除网络拓扑数据</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see NetworkTopologyInfo 网络拓扑信息实体类
 */
@Mapper
public interface NetworkTopologyMapper extends BaseMapper<NetworkTopologyInfo> {

	/**
	 * 根据配置ID查询网络拓扑信息列表
	 * <p>
	 * 通过配置ID查询该配置下的所有网络拓扑信息，用于获取完整的网络拓扑结构
	 *
	 * @param configId 配置ID
	 * @return 网络拓扑信息列表
	 */
	List<NetworkTopologyInfo> findByConfigId(@Param("configId") String configId);

	/**
	 * 根据MAC地址查询网络拓扑信息列表
	 * <p>
	 * 通过MAC地址查询相关的网络拓扑信息，用于定位特定设备在网络中的位置
	 *
	 * @param mac MAC地址
	 * @return 网络拓扑信息列表
	 */
	List<NetworkTopologyInfo> findByMac(@Param("mac") String mac);

	/**
	 * 根据配置ID删除所有关联的网络拓扑数据
	 * <p>
	 * 删除指定配置下的所有网络拓扑信息，通常用于配置删除或重置场景
	 *
	 * @param configId 配置ID
	 */
	void deleteAllByConfigId(@Param("configId") String configId);

}
