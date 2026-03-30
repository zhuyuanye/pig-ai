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

package com.pig4cloud.pig.alerter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.pig4cloud.pig.common.alert.service.NoticeConfigService;
import com.pig4cloud.pig.common.core.entity.alerter.NoticeReceiver;
import com.pig4cloud.pig.common.core.entity.alerter.NoticeRule;
import com.pig4cloud.pig.common.core.entity.alerter.NoticeTemplate;
import com.pig4cloud.pig.common.core.entity.dto.Message;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.pig4cloud.pig.common.core.constants.CommonConstants.FAIL_CODE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 消息通知配置管理控制器
 * <p>提供通知接收人、通知规则、通知模板的增删改查等操作接口</p>
 *
 * @author pig4cloud
 */
@Tag(name = "Notification Config API")
@RestController()
@RequestMapping(value = "/api/notice", produces = {APPLICATION_JSON_VALUE})
public class NoticeConfigController {

    @Autowired
    private NoticeConfigService noticeConfigService;

    /**
     * 新增通知接收人
     *
     * @param noticeReceiver 通知接收人信息
     * @return 操作结果
     */
    @PostMapping(path = "/receiver")
    @Operation(summary = "Add a recipient", description = "Add a recipient")
    public ResponseEntity<Message<Void>> addNewNoticeReceiver(@Valid @RequestBody NoticeReceiver noticeReceiver) {
        noticeConfigService.addReceiver(noticeReceiver);
        return ResponseEntity.ok(Message.success("Add success"));
    }

    /**
     * 修改已有的通知接收人信息
     *
     * @param noticeReceiver 通知接收人信息
     * @return 操作结果
     */
    @PutMapping(path = "/receiver")
    @Operation(summary = "Modify existing recipient information", description = "Modify existing recipient information")
    public ResponseEntity<Message<Void>> editNoticeReceiver(@Valid @RequestBody NoticeReceiver noticeReceiver) {
        noticeConfigService.editReceiver(noticeReceiver);
        return ResponseEntity.ok(Message.success("Edit success"));
    }

    /**
     * 根据接收人ID删除通知接收人
     *
     * @param receiverId 接收人ID
     * @return 操作结果
     */
    @DeleteMapping(path = "/receiver/{id}")
    @Operation(summary = "Delete existing recipient information", description = "Delete existing recipient information")
    public ResponseEntity<Message<Void>> deleteNoticeReceiver(
            @Parameter(description = "en: Recipient ID", example = "6565463543") @PathVariable("id") final Long receiverId) {
        NoticeReceiver noticeReceiver = noticeConfigService.getReceiverById(receiverId);
        if (noticeReceiver == null) {
            return ResponseEntity.ok(Message.success("The relevant information of the recipient could not be found, please check whether the parameters are correct"));
        }
        noticeConfigService.deleteReceiver(receiverId);
        return ResponseEntity.ok(Message.success("Delete success"));
    }

    /**
     * 根据查询过滤条件获取通知接收人列表（分页）
     *
     * @param name      接收人名称，支持模糊查询
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 通知接收人分页列表
     */
    @GetMapping(path = "/receivers")
    @Operation(summary = "Get a list of message notification recipients based on query filter items",
            description = "Get a list of message notification recipients based on query filter items")
    public ResponseEntity<Message<IPage<NoticeReceiver>>> getReceivers(
            @Parameter(description = "en: Recipient name,support fuzzy query", example = "tom") @RequestParam(required = false) final String name,
            @Parameter(description = "en: List current page", example = "0") @RequestParam(defaultValue = "0") final int pageIndex,
            @Parameter(description = "en: Number of list pages", example = "8") @RequestParam(defaultValue = "8") final int pageSize) {
        return ResponseEntity.ok(Message.success(noticeConfigService.getNoticeReceivers(name, pageIndex, pageSize)));
    }

    /**
     * 获取所有通知接收人列表
     *
     * @return 所有通知接收人列表
     */
    @GetMapping(path = "/receivers/all")
    @Operation(summary = "Get a list of all message notification recipients",
            description = "Get a list of all message notification recipients")
    public ResponseEntity<Message<List<NoticeReceiver>>> getAllReceivers() {
        return ResponseEntity.ok(Message.success(noticeConfigService.getAllNoticeReceivers()));
    }

    /**
     * 根据接收人ID获取通知接收人详情
     *
     * @param receiverId 接收人ID
     * @return 通知接收人信息
     */
    @GetMapping(path = "/receiver/{id}")
    @Operation(summary = "Get the recipient information based on the recipient ID",
            description = "Get the recipient information based on the recipient ID")
    public ResponseEntity<Message<NoticeReceiver>> getReceiverById(
            @Parameter(description = "en: Recipient ID", example = "6565463543") @PathVariable("id") final Long receiverId) {
        NoticeReceiver noticeReceiver = noticeConfigService.getReceiverById(receiverId);
        if (noticeReceiver == null) {
            return ResponseEntity.ok(Message.fail(FAIL_CODE, "The relevant information of the recipient could not be found, please check whether the parameters are correct or refresh the page"));
        }
        return ResponseEntity.ok(Message.success(noticeReceiver));
    }

    /**
     * 新增通知规则
     *
     * @param noticeRule 通知规则信息
     * @return 操作结果
     */
    @PostMapping(path = "/rule")
    @Operation(summary = "Add a notification policy", description = "Add a notification policy")
    public ResponseEntity<Message<Void>> addNewNoticeRule(@Valid @RequestBody NoticeRule noticeRule) {
        noticeConfigService.addNoticeRule(noticeRule);
        return ResponseEntity.ok(Message.success("Add success"));
    }

    /**
     * 修改已有的通知规则信息
     *
     * @param noticeRule 通知规则信息
     * @return 操作结果
     */
    @PutMapping(path = "/rule")
    @Operation(summary = "Modify existing notification policy information", description = "Modify existing notification policy information")
    public ResponseEntity<Message<Void>> editNoticeRule(@Valid @RequestBody NoticeRule noticeRule) {
        noticeConfigService.editNoticeRule(noticeRule);
        return ResponseEntity.ok(Message.success("Edit success"));
    }

    /**
     * 根据通知规则ID删除通知规则
     *
     * @param ruleId 通知规则ID
     * @return 操作结果
     */
    @DeleteMapping(path = "/rule/{id}")
    @Operation(summary = "Delete existing notification policy information", description = "Delete existing notification policy information")
    public ResponseEntity<Message<Void>> deleteNoticeRule(
            @Parameter(description = "en: Notification Policy ID", example = "6565463543") @PathVariable("id") final Long ruleId) {
        // Returns success if it does not exist or if the deletion is successful
        NoticeRule noticeRule = noticeConfigService.getNoticeRulesById(ruleId);
        if (noticeRule == null) {
            return ResponseEntity.ok(Message.success("The specified notification rule could not be queried, please check whether the parameters are correct"));
        }
        noticeConfigService.deleteNoticeRule(ruleId);
        return ResponseEntity.ok(Message.success("Delete success"));
    }

    /**
     * 根据查询过滤条件获取通知规则列表（分页）
     *
     * @param name      通知规则名称
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 通知规则分页列表
     */
    @GetMapping(path = "/rules")
    @Operation(summary = "Get a list of message notification policies based on query filter items",
            description = "Get a list of message notification policies based on query filter items")
    public ResponseEntity<Message<IPage<NoticeRule>>> getRules(
            @Parameter(description = "en: Recipient name", example = "rule1") @RequestParam(required = false) final String name,
            @Parameter(description = "en: List current page", example = "0") @RequestParam(defaultValue = "0") final int pageIndex,
            @Parameter(description = "en: Number of list pages", example = "8") @RequestParam(defaultValue = "8") final int pageSize) {
        return ResponseEntity.ok(Message.success(noticeConfigService.getNoticeRules(name, pageIndex, pageSize)));
    }

    /**
     * 根据通知规则ID获取通知规则详情
     *
     * @param ruleId 通知规则ID
     * @return 通知规则信息
     */
    @GetMapping(path = "/rule/{id}")
    @Operation(summary = "Get the notification policy information based on the policy ID",
            description = "Get the notification policy information based on the policy ID")
    public ResponseEntity<Message<NoticeRule>> getRuleById(
            @Parameter(description = "en: Notification Policy ID", example = "6565463543") @PathVariable("id") final Long ruleId) {
        NoticeRule noticeRule = noticeConfigService.getNoticeRulesById(ruleId);
        if (noticeRule == null) {
            return ResponseEntity.ok(Message.fail(FAIL_CODE, "The specified notification rule could not be queried, please check whether the parameters are correct or refresh the page"));
        }
        return ResponseEntity.ok(Message.success(noticeRule));
    }

    /**
     * 新增通知模板
     *
     * @param noticeTemplate 通知模板信息
     * @return 操作结果
     */
    @PostMapping(path = "/template")
    @Operation(summary = "Add a notification template", description = "Add a notification template")
    public ResponseEntity<Message<Void>> addNewNoticeTemplate(@Valid @RequestBody NoticeTemplate noticeTemplate) {
        noticeConfigService.addNoticeTemplate(noticeTemplate);
        return ResponseEntity.ok(Message.success("Add success"));
    }

    /**
     * 修改已有的通知模板信息
     *
     * @param noticeTemplate 通知模板信息
     * @return 操作结果
     */
    @PutMapping(path = "/template")
    @Operation(summary = "Modify existing notification template information", description = "Modify existing notification template information")
    public ResponseEntity<Message<Void>> editNoticeTemplate(@Valid @RequestBody NoticeTemplate noticeTemplate) {
        noticeConfigService.editNoticeTemplate(noticeTemplate);
        return ResponseEntity.ok(Message.success("Edit success"));
    }

    /**
     * 根据通知模板ID删除通知模板
     *
     * @param templateId 通知模板ID
     * @return 操作结果
     */
    @DeleteMapping(path = "/template/{id}")
    @Operation(summary = "Delete existing notification template information", description = "Delete existing notification template information")
    public ResponseEntity<Message<Void>> deleteNoticeTemplate(
            @Parameter(description = "en: Notification template ID", example = "6565463543") @PathVariable("id") final Long templateId) {
        // Returns success if it does not exist or if the deletion is successful
        Optional<NoticeTemplate> noticeTemplate = noticeConfigService.getNoticeTemplatesById(templateId);
        if (noticeTemplate.isEmpty()) {
            return ResponseEntity.ok(Message.success("The specified notification template could not be queried, please check whether the parameters are correct"));
        }
        noticeConfigService.deleteNoticeTemplate(templateId);
        return ResponseEntity.ok(Message.success("Delete success"));
    }

    /**
     * 根据查询过滤条件获取通知模板列表（分页）
     *
     * @param name      模板名称，支持模糊查询
     * @param preset    是否为预设模板
     * @param pageIndex 当前页码
     * @param pageSize  每页数量
     * @return 通知模板分页列表
     */
    @GetMapping(path = "/templates")
    @Operation(summary = "Get a list of message notification templates based on query filter items",
            description = "Get a list of message notification templates based on query filter items")
    public ResponseEntity<Message<IPage<NoticeTemplate>>> getTemplates(
            @Parameter(description = "Template name,support fuzzy query", example = "rule1") @RequestParam(required = false) final String name,
            @Parameter(description = "Whether it is a preset template", example = "true") @RequestParam(defaultValue = "true") final boolean preset,
            @Parameter(description = "List current page", example = "0") @RequestParam(defaultValue = "0") final int pageIndex,
            @Parameter(description = "Number of list pages", example = "8") @RequestParam(defaultValue = "8") final int pageSize) {
        IPage<NoticeTemplate> templatePage = noticeConfigService.getNoticeTemplates(name, preset, pageIndex, pageSize);
        return ResponseEntity.ok(Message.success(templatePage));
    }

    /**
     * 获取所有通知模板列表
     *
     * @return 所有通知模板列表
     */
    @GetMapping(path = "/templates/all")
    @Operation(summary = "Get a list of all message notification templates",
            description = "Get a list of all message notification templates")
    public ResponseEntity<Message<List<NoticeTemplate>>> getAllTemplates() {
        return ResponseEntity.ok(Message.success(noticeConfigService.getAllNoticeTemplates()));
    }

    /**
     * 根据通知模板ID获取通知模板详情
     *
     * @param templateId 通知模板ID
     * @return 通知模板信息
     */
    @GetMapping(path = "/template/{id}")
    @Operation(summary = "Get the notification template information based on the template ID",
            description = "Get the notification template information based on the template ID")
    public ResponseEntity<Message<NoticeTemplate>> getTemplateById(
            @Parameter(description = "en: Notification template ID", example = "6565463543") @PathVariable("id") final Long templateId) {
        Optional<NoticeTemplate> noticeTemplate = noticeConfigService.getNoticeTemplatesById(templateId);
        if (noticeTemplate.isEmpty()) {
            return ResponseEntity.ok(Message.fail(FAIL_CODE, "The specified notification template could not be queried, please check whether the parameters are correct or refresh the page"));
        }
        return ResponseEntity.ok(Message.success(noticeTemplate.get()));
    }

    /**
     * 发送测试消息给指定接收人
     *
     * @param noticeReceiver 通知接收人信息
     * @return 操作结果
     */
    @PostMapping(path = "/receiver/send-test-msg")
    @Operation(summary = "Send test msg to receiver", description = "Send test msg to receiver")
    public ResponseEntity<Message<Void>> sendTestMsg(@Valid @RequestBody NoticeReceiver noticeReceiver) {
        boolean sendFlag = noticeConfigService.sendTestMsg(noticeReceiver);
        if (sendFlag) {
            return ResponseEntity.ok(Message.success());
        }
        return ResponseEntity.ok(Message.fail(FAIL_CODE, "Notify service not available, please check config!"));
    }
}
