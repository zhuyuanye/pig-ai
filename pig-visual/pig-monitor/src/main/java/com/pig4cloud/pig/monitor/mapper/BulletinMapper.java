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
import com.pig4cloud.pig.common.core.entity.manager.Bulletin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告 Mapper 接口
 * <p>
 * 负责系统公告数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>系统公告的基础增删改查操作</li>
 *   <li>根据名称列表批量删除公告</li>
 *   <li>根据名称查询公告详情</li>
 *   <li>根据名称统计公告数量</li>
 *   <li>管理系统公告的发布和维护</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see Bulletin 系统公告实体类
 */
@Mapper
public interface BulletinMapper extends BaseMapper<Bulletin> {

	/**
	 * 根据名称列表批量删除公告
	 * <p>
	 * 批量删除指定名称的公告，通常用于公告清理或批量管理操作
	 *
	 * @param names 公告名称列表
	 */
	void deleteByNameIn(@Param("names") List<String> names);

	/**
	 * 根据名称查询公告
	 * <p>
	 * 通过公告的唯一名称查询对应的公告详情
	 * <p>
	 * 注意：公告名称在系统中应该是唯一的，此方法只返回第一个匹配的结果
	 *
	 * @param name 公告名称
	 * @return 公告对象，如果不存在则返回null
	 */
	Bulletin findByName(@Param("name") String name);

	/**
	 * 根据名称统计公告数量
	 * <p>
	 * 统计指定名称的公告数量，通常用于验证公告名称是否已存在
	 *
	 * @param name 公告名称
	 * @return 公告数量
	 */
	int countByName(@Param("name") String name);

}
