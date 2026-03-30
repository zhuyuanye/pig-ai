/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.pig4cloud.pig.alerter.controller;

import com.pig4cloud.pig.common.alert.config.AlertSseManager;
import com.pig4cloud.pig.common.core.util.SnowFlakeIdGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * 告警SSE（Server-Sent Events）推送控制器
 * <p>提供告警事件的实时推送订阅接口，客户端通过SSE协议接收告警通知</p>
 *
 * @author pig4cloud
 */
@RestController
@RequestMapping(path = "/api/alert/sse", produces = {TEXT_EVENT_STREAM_VALUE})
public class AlertSseController {

    private final AlertSseManager emitterManager;

    public AlertSseController(AlertSseManager emitterManager) {
        this.emitterManager = emitterManager;
    }

    /**
     * 订阅告警事件推送
     * <p>客户端调用此接口建立SSE连接，实时接收告警事件通知</p>
     *
     * @return SSE事件发射器
     */
    @GetMapping(path = "/subscribe")
    public SseEmitter subscribe() {
        Long clientId = SnowFlakeIdGenerator.generateId();
        return emitterManager.createEmitter(clientId);
    }
}
