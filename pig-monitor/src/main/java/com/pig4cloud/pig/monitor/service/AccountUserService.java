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

package com.pig4cloud.pig.monitor.service;

import com.pig4cloud.pig.common.core.entity.user.HzbUser;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Account service
 */
public interface AccountUserService {

    /**
     * 新增用户
     * @param hzbUser 用户实体
     * @return 新增的用户实体
     */
    HzbUser addUser(HzbUser hzbUser);

    /**
     * 根据 ID 查询用户
     * @param userId 用户 ID
     * @return 用户实体
     */
    HzbUser getUserById(Long userId);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户实体
     */
    HzbUser getUserByUsername(String username);

    /**
     * 修改用户信息
     * @param hzbUser 用户实体
     * @return 更新后的用户实体
     */
    HzbUser updateUser(HzbUser hzbUser);

    /**
     * 根据 ID 删除用户
     * @param userId 用户 ID
     */
    void deleteUser(Long userId);

    /**
     * 分页查询用户
     * @param username 用户名
     * @param page 页码
     * @param size 数量
     * @return 分页
     */
    IPage<HzbUser> getUsersByPage(String username, int page, int size);


}
