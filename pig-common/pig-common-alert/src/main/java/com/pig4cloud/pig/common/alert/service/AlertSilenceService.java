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
import com.pig4cloud.pig.common.core.entity.alerter.AlertSilence;

import java.util.List;
import java.util.Set;

/**
 * 告警静默管理服务接口
 */
public interface AlertSilenceService {

    /**
     * 校验请求数据参数的正确性
     * @param alertSilence 告警静默实体
     * @param isModify     是否为修改操作
     * @throws IllegalArgumentException 校验参数错误时抛出
     */
    void validate(AlertSilence alertSilence, boolean isModify) throws IllegalArgumentException;

    /**
     * 新增告警静默
     * @param alertSilence 告警静默实体
     * @throws RuntimeException 新增过程中的异常抛出
     */
    void addAlertSilence(AlertSilence alertSilence) throws RuntimeException;

    /**
     * 修改告警静默
     * @param alertSilence 告警静默实体
     * @throws RuntimeException 修改过程中的异常抛出
     */
    void modifyAlertSilence(AlertSilence alertSilence) throws RuntimeException;

    /**
     * 获取告警静默信息
     * @param silenceId 告警静默ID
     * @return AlertSilence 告警静默实体
     * @throws RuntimeException 查询过程中的异常抛出
     */
    AlertSilence getAlertSilence(long silenceId) throws RuntimeException;

    /**
     * 批量删除告警静默
     * @param silenceIds 告警静默ID集合
     * @throws RuntimeException 删除过程中的异常抛出
     */
    void deleteAlertSilences(Set<Long> silenceIds) throws RuntimeException;

    /**
     * 动态条件查询告警静默
     * @param silenceIds 告警静默ID列表
     * @param search     搜索关键字
     * @param sort       排序字段
     * @param order      排序方式: asc-升序, desc-降序
     * @param pageIndex  当前页码
     * @param pageSize   每页数量
     * @return 分页查询结果
     */
    IPage<AlertSilence> getAlertSilences(List<Long> silenceIds, String search, String sort, String order, int pageIndex, int pageSize);
}
