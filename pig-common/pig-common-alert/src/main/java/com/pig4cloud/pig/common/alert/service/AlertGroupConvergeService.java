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

package com.pig4cloud.pig.common.alert.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pig.common.core.entity.alerter.AlertGroupConverge;

import java.util.List;
import java.util.Set;

/**
 * 告警收敛管理服务接口
 */
public interface AlertGroupConvergeService {

    /**
     * 校验请求数据参数的正确性
     * @param alertGroupConverge 告警收敛实体
     * @param isModify           是否为修改操作
     * @throws IllegalArgumentException 校验参数错误时抛出
     */
    void validate(AlertGroupConverge alertGroupConverge, boolean isModify) throws IllegalArgumentException;

    /**
     * 新增告警收敛
     * @param alertGroupConverge 告警收敛实体
     * @throws RuntimeException 新增过程中的异常抛出
     */
    void addAlertGroupConverge(AlertGroupConverge alertGroupConverge) throws RuntimeException;

    /**
     * 修改告警收敛
     * @param alertGroupConverge 告警收敛实体
     * @throws RuntimeException 修改过程中的异常抛出
     */
    void modifyAlertGroupConverge(AlertGroupConverge alertGroupConverge) throws RuntimeException;

    /**
     * 获取告警收敛信息
     * @param convergeId 告警收敛ID
     * @return AlertGroupConverge 告警收敛实体
     * @throws RuntimeException 查询过程中的异常抛出
     */
    AlertGroupConverge getAlertGroupConverge(long convergeId) throws RuntimeException;

    /**
     * 批量删除告警收敛
     * @param convergeIds 告警收敛ID集合
     * @throws RuntimeException 删除过程中的异常抛出
     */
    void deleteAlertGroupConverges(Set<Long> convergeIds) throws RuntimeException;

    /**
     * 动态条件查询告警收敛
     * @param convergeIds 告警收敛ID列表
     * @param search      搜索关键字
     * @param sort        排序字段
     * @param order       排序方式: asc-升序, desc-降序
     * @param pageIndex   当前页码
     * @param pageSize    每页数量
     * @return 分页查询结果
     */
    IPage<AlertGroupConverge> getAlertGroupConverges(List<Long> convergeIds, String search, String sort, String order, int pageIndex, int pageSize);
}
