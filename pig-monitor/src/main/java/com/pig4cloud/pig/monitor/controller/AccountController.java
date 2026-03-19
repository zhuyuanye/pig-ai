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
import com.pig4cloud.pig.common.core.entity.user.HzbUser;
import com.pig4cloud.pig.common.core.util.ResponseUtil;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.monitor.pojo.dto.LoginDto;
import com.pig4cloud.pig.monitor.pojo.dto.RefreshTokenResponse;
import com.pig4cloud.pig.monitor.pojo.dto.TokenDto;
import com.pig4cloud.pig.monitor.service.AccountService;
import com.pig4cloud.pig.monitor.service.AccountUserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.Map;

import static com.pig4cloud.pig.common.core.constants.CommonConstants.LOGIN_FAILED_CODE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Authentication registration TOKEN management API
 * <p>
 * 认证统一由 pig-auth (OAuth2) 处理，本 Controller 保留 HertzBeat 原有的
 * sureness JWT token 相关接口用于兼容，登录/登出由 OAuth2 流程完成。
 */
@Tag(name = "Auth Manage API")
@RestController
@RequestMapping(value = "/api/account/auth", produces = {APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    private final AccountUserService accountUserService;

    @Inner
    @PostMapping("/form")
    @Operation(summary = "Account password login to obtain associated user information",
            description = "Account password login to obtain associated user information")
    public ResponseEntity<Message<Map<String, String>>> authGetToken(@Valid @RequestBody LoginDto loginDto) {
        return ResponseUtil.handle(() -> accountService.authGetToken(loginDto));
    }

    @Inner
    @PostMapping("/refresh")
    @Operation(summary = "Use refresh TOKEN to re-acquire TOKEN",
            description = "Use refresh TOKEN to re-acquire TOKEN")
    public ResponseEntity<Message<RefreshTokenResponse>> refreshToken(@Valid @RequestBody TokenDto tokenDto) {
        try {
            return ResponseEntity.ok(Message.success(accountService.refreshToken(tokenDto.getToken())));
        } catch (AuthenticationException e) {
            return ResponseEntity.ok(Message.fail(LOGIN_FAILED_CODE, e.getMessage()));
        } catch (ExpiredJwtException expiredJwtException) {
            log.warn("{}", expiredJwtException.getMessage());
            return ResponseEntity.ok(Message.fail(LOGIN_FAILED_CODE, "Refresh Token Expired"));
        } catch (Exception e) {
            log.error("Exception occurred during token refresh: {}", e.getClass().getName(), e);
            return ResponseEntity.ok(Message.fail(LOGIN_FAILED_CODE, "Refresh Token Error"));
        }
    }

    /**
     * 校验是否已登录 - 通过 OAuth2 token 判断
     */
    @Operation(summary = "校验是否已登录", description = "检查当前用户是否已经通过 OAuth2 认证")
    @GetMapping("/is_login")
    public ResponseEntity<String> isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.ok("已登录");
        } else {
            return ResponseEntity.status(401).body("未登录");
        }
    }

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户", description = "使用此接口创建一个新用户。返回新创建的用户实体。")
    @PostMapping
    public ResponseEntity<HzbUser> addUser(@RequestBody HzbUser hzbUser) {
        hzbUser.setRole("user");
        HzbUser createdUser = accountUserService.addUser(hzbUser);
        return ResponseEntity.ok(createdUser);
    }

    /**
     * 根据 ID 查询用户
     */
    @Operation(summary = "根据 ID 查询用户", description = "根据给定的用户 ID 获取用户的详细信息。")
    @GetMapping("/{userId}")
    public ResponseEntity<HzbUser> getUserById(@PathVariable Long userId) {
        HzbUser user = accountUserService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据用户名查询用户
     */
    @Operation(summary = "根据用户名查询用户", description = "根据给定的用户名获取用户的详细信息。")
    @GetMapping("/username/{username}")
    public ResponseEntity<HzbUser> getUserByUsername(@PathVariable String username) {
        HzbUser user = accountUserService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 修改用户信息
     */
    @Operation(summary = "修改用户信息", description = "使用此接口更新用户信息，并返回更新后的用户实体。")
    @PutMapping("/{userId}")
    public ResponseEntity<HzbUser> updateUser(@PathVariable Long userId, @RequestBody HzbUser hzbUser) {
        hzbUser.setId(userId);
        HzbUser updatedUser = accountUserService.updateUser(hzbUser);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 根据 ID 删除用户
     */
    @Operation(summary = "根据 ID 删除用户", description = "使用此接口删除指定 ID 的用户。成功删除后，返回无内容的响应。")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        accountUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 分页查询用户（可模糊搜索用户名）
     */
    @Operation(summary = "分页查询用户（可模糊搜索用户名）", description = "根据用户名关键字和分页参数查询用户列表")
    @GetMapping("/page")
    public Page<HzbUser> getUsersByPage(
            @Parameter(description = "用户名关键字（可选）", example = "admin") @RequestParam(required = false) String username,
            @Parameter(description = "页码（从1开始）", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int size
    ) {
        page = page - 1;
        return accountUserService.getUsersByPage(username, page, size);
    }

}
