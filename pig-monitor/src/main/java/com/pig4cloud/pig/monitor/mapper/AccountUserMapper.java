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
import com.pig4cloud.pig.common.core.entity.user.HzbUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 账户用户 Mapper 接口
 * <p>
 * 负责账户用户数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>用户信息的基础增删改查操作</li>
 *   <li>根据用户名查询用户信息</li>
 *   <li>用户认证和授权相关数据操作</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see HzbUser 账户用户实体类
 */
@Mapper
public interface AccountUserMapper extends BaseMapper<HzbUser> {

	/**
	 * 根据用户名查询用户信息
	 * <p>
	 * 通过用户名查询用户的详细信息，主要用于用户登录认证
	 * <p>
	 * 注意：用户名在系统中应该是唯一的，此方法只返回第一个匹配的结果
	 *
	 * @param username 用户名
	 * @return 用户对象，如果不存在则返回null
	 */
	HzbUser findByUsername(@Param("username") String username);

}
