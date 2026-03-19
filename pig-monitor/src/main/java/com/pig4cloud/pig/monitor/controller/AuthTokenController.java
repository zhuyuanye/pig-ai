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

package com.pig4cloud.pig.monitor.controller;

import com.pig4cloud.pig.common.core.entity.dto.Message;
import com.pig4cloud.pig.monitor.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static com.pig4cloud.pig.common.core.constants.CommonConstants.FAIL_CODE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Generate TOKEN API
 * <p>
 * 使用 Spring Security (OAuth2) 替代 sureness 进行权限校验
 */
@Tag(name = "Generate TOKEN API")
@RestController
@RequestMapping(value = "/api/account/token", produces = {APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Slf4j
public class AuthTokenController {

    private final AccountService accountService;

    @PostMapping("/generate")
    @Operation(summary = "Generate a long-lived API token", description = "Generate a long-lived API token for the current authenticated user")
    public ResponseEntity<Message<Map<String, String>>> generateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(Message.fail(FAIL_CODE, "No login user"));
        }
        // 检查是否有 admin 角色
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().contains("ROLE_admin") || a.getAuthority().contains("admin"));
        if (!isAdmin) {
            return ResponseEntity.ok(Message.fail(FAIL_CODE, "No permission"));
        }
        try {
            String token = accountService.generateToken();
            Map<String, String> rep = Collections.singletonMap("token", token);
            return ResponseEntity.ok(Message.success(rep));
        } catch (Exception e) {
            log.error("generate token error", e);
            return ResponseEntity.ok(Message.fail(FAIL_CODE, "Generate token error"));
        }
    }
}
