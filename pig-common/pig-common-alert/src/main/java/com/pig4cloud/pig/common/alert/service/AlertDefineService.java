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
import com.pig4cloud.pig.common.core.entity.alerter.AlertDefine;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * 告警定义管理服务接口
 */
public interface AlertDefineService {

    /**
     * 校验请求数据参数的正确性
     * @param alertDefine 告警定义实体
     * @param isModify 是否为修改操作
     * @throws IllegalArgumentException 校验参数错误时抛出
     */
    void validate(AlertDefine alertDefine, boolean isModify) throws IllegalArgumentException;

    /**
     * 新增告警定义
     * @param alertDefine 告警定义实体
     * @throws RuntimeException 新增过程中的异常抛出
     */
    void addAlertDefine(AlertDefine alertDefine) throws RuntimeException;

    /**
     * 修改告警定义
     * @param alertDefine 告警定义实体
     * @throws RuntimeException 修改过程中的异常抛出
     */
    void modifyAlertDefine(AlertDefine alertDefine) throws RuntimeException;

    /**
     * 删除告警定义
     * @param alertId 告警定义ID
     * @throws RuntimeException 删除过程中的异常抛出
     */
    void deleteAlertDefine(long alertId) throws RuntimeException;

    /**
     * 获取告警定义信息
     * @param alertId 告警定义ID
     * @return AlertDefine 告警定义实体
     * @throws RuntimeException 查询过程中的异常抛出
     */
    AlertDefine getAlertDefine(long alertId) throws RuntimeException;

    /**
     * 批量删除告警定义
     * @param alertIds 告警定义ID集合
     * @throws RuntimeException 删除过程中的异常抛出
     */
    void deleteAlertDefines(Set<Long> alertIds) throws RuntimeException;

    /**
     * 动态条件查询告警定义
     * @param defineIds 告警定义ID列表
     * @param search    搜索关键字-目标表达式模板
     * @param sort      排序字段
     * @param order     排序方式: asc-升序, desc-降序
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 分页查询结果
     */
    IPage<AlertDefine> getAlertDefines(List<Long> defineIds, String search, String sort, String order, int pageIndex, int pageSize);

    /**
     * 根据ID列表和导出文件类型导出指定类型的文件配置
     * @param ids  告警定义ID列表
     * @param type 文件类型
     * @param res  HTTP响应对象
     * @throws Exception 导出过程中的异常抛出
     */
    void export(List<Long> ids, String type, HttpServletResponse res) throws Exception;

    /**
     * 根据上传的告警阈值文件添加告警阈值规则
     * @param file 上传的文件
     * @throws Exception 导入过程中的异常抛出
     */
    void importConfig(MultipartFile file) throws Exception;

    /**
     * 获取实时告警定义列表
     * @return 实时告警定义列表
     */
    List<AlertDefine> getRealTimeAlertDefines();

}
