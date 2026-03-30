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
import com.pig4cloud.pig.common.core.entity.alerter.GroupAlert;
import com.pig4cloud.pig.common.core.entity.alerter.NoticeReceiver;
import com.pig4cloud.pig.common.core.entity.alerter.NoticeRule;
import com.pig4cloud.pig.common.core.entity.alerter.NoticeTemplate;

import java.util.List;
import java.util.Optional;

/**
 * 消息通知配置服务接口
 */
public interface NoticeConfigService {

    /**
     * 动态条件查询通知接收人
     * @param name      接收人名称，支持模糊查询
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 分页查询结果
     */
    IPage<NoticeReceiver> getNoticeReceivers(String name, int pageIndex, int pageSize);

    /**
     * 动态条件查询通知模板
     * @param name      模板名称，支持模糊查询
     * @param preset    是否为系统预设模板（true: 系统预设模板, false: 自定义模板）
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 分页查询结果
     */
    IPage<NoticeTemplate> getNoticeTemplates(String name, boolean preset, int pageIndex, int pageSize);

    /**
     * 动态条件查询通知规则
     * @param name      规则名称，支持模糊查询
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 分页查询结果
     */
    IPage<NoticeRule> getNoticeRules(String name, int pageIndex, int pageSize);

    /**
     * 新增通知接收人
     * @param noticeReceiver 接收人信息
     */
    void addReceiver(NoticeReceiver noticeReceiver);

    /**
     * 修改通知接收人
     * @param noticeReceiver 接收人信息
     */
    void editReceiver(NoticeReceiver noticeReceiver);

    /**
     * 根据接收人ID删除接收人信息
     * @param receiverId 接收人ID
     */
    void deleteReceiver(Long receiverId);

    /**
     * 新增通知策略
     * @param noticeRule 通知策略实体
     */
    void addNoticeRule(NoticeRule noticeRule);

    /**
     * 修改通知策略
     * @param noticeRule 通知策略实体
     */
    void editNoticeRule(NoticeRule noticeRule);

    /**
     * 删除指定通知策略
     * @param ruleId 通知策略ID
     */
    void deleteNoticeRule(Long ruleId);

    /**
     * 根据告警信息匹配所有通知策略，筛选出需要通知的接收人
     * @param alert 告警信息
     * @return 匹配的通知规则列表
     */
    List<NoticeRule> getReceiverFilterRule(GroupAlert alert);

    /**
     * 根据模板ID查询模板信息
     * @param id 模板ID
     * @return 通知模板实体
     */
    NoticeTemplate getOneTemplateById(Long id);

    /**
     * 根据接收人ID（主键ID）查询接收人信息
     * @param receiverId 接收人ID（主键ID）
     * @return 接收人实体
     */
    NoticeReceiver getReceiverById(Long receiverId);

    /**
     * 根据规则ID（主键ID）查询指定通知规则
     * @param ruleId 规则ID
     * @return 通知规则实体
     */
    NoticeRule getNoticeRulesById(Long ruleId);

    /**
     * 新增通知模板
     * @param noticeTemplate 模板信息
     */
    void addNoticeTemplate(NoticeTemplate noticeTemplate);

    /**
     * 修改通知模板
     * @param noticeTemplate 模板信息
     */
    void editNoticeTemplate(NoticeTemplate noticeTemplate);

    /**
     * 根据模板ID删除模板信息
     * @param templateId 模板ID
     */
    void deleteNoticeTemplate(Long templateId);

    /**
     * 根据模板ID（主键ID）查询指定通知模板
     * @param templateId 模板ID
     * @return 通知模板实体（Optional包装）
     */
    Optional<NoticeTemplate> getNoticeTemplatesById(Long templateId);

    /**
     * 根据模板类型查询默认通知模板
     * @param type 模板类型
     * @return 通知模板实体
     */
    NoticeTemplate getDefaultNoticeTemplateByType(Byte type);

    /**
     * 发送测试消息
     * @param noticeReceiver 接收人信息
     * @return true-发送成功 | false-发送失败
     */
    boolean sendTestMsg(NoticeReceiver noticeReceiver);

    /**
     * 查询所有通知接收人
     * @return 接收人列表
     */
    List<NoticeReceiver> getAllNoticeReceivers();

    /**
     * 查询所有通知模板
     * @return 通知模板列表
     */
    List<NoticeTemplate> getAllNoticeTemplates();
}
