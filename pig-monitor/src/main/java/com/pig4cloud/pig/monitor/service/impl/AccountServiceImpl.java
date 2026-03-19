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

package com.pig4cloud.pig.monitor.service.impl;

import com.alibaba.fastjson.JSON;
import com.pig4cloud.pig.common.core.entity.user.HzbUser;
import com.pig4cloud.pig.common.core.util.JsonUtil;
import com.pig4cloud.pig.monitor.pojo.dto.LoginDto;
import com.pig4cloud.pig.monitor.pojo.dto.RefreshTokenResponse;
import com.pig4cloud.pig.monitor.service.AccountService;
import com.pig4cloud.pig.monitor.service.AccountUserService;
import com.pig4cloud.pig.monitor.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.*;

/**
 * Implementation of Account service
 * <p>
 * 使用自定义 JwtTokenUtil 替代 sureness 的 JsonWebTokenUtil
 */
@Service
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountUserService accountUserService;

    /**
     * Token validity time in seconds
     */
    private static final long PERIOD_TIME = 3600L;

    @Override
    public Map<String, String> authGetToken(LoginDto loginDto) throws AuthenticationException {
        HzbUser user = accountUserService.getUserByUsername(loginDto.getIdentifier());
        log.info("authGetToken:user:{}", JSON.toJSONString(user));
        if (user == null || StringUtils.isBlank(user.getPassword())) {
            log.error("getUserByUsername: user == null || password is blank");
            throw new AuthenticationException("Incorrect Account or Password");
        }

        String password = loginDto.getCredential();
        if (!user.getPassword().equals(password)) {
            log.error("getUserByUsername: password invalid");
            throw new AuthenticationException("Incorrect Account or Password");
        }

        List<String> roles = Arrays.asList(user.getRole().split(","));
        // Issue TOKEN
        String issueToken = JwtTokenUtil.issueJwt(loginDto.getIdentifier(), PERIOD_TIME, roles, null);
        Map<String, Object> customClaimMap = new HashMap<>(1);
        customClaimMap.put("refresh", true);
        String issueRefresh = JwtTokenUtil.issueJwt(loginDto.getIdentifier(), PERIOD_TIME << 5, roles, customClaimMap);
        Map<String, String> resp = new HashMap<>(3);
        resp.put("token", issueToken);
        resp.put("refreshToken", issueRefresh);
        resp.put("role", JsonUtil.toJson(roles));
        return resp;
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) throws Exception {
        Claims claims = JwtTokenUtil.parseJwt(refreshToken);
        String userId = String.valueOf(claims.getSubject());
        Boolean isRefresh = claims.get("refresh", Boolean.class);
        if (StringUtils.isBlank(userId) || isRefresh == null || !isRefresh) {
            throw new AuthenticationException("Illegal Refresh Token");
        }
        HzbUser user = accountUserService.getUserByUsername(userId);
        if (user == null) {
            throw new AuthenticationException("Not Exists This Token Mapping Account");
        }
        List<String> roles = Arrays.asList(user.getRole().split(","));
        String issueToken = JwtTokenUtil.issueJwt(userId, PERIOD_TIME, roles, null);
        Map<String, Object> customClaimMap = new HashMap<>(1);
        customClaimMap.put("refresh", true);
        String issueRefresh = JwtTokenUtil.issueJwt(userId, PERIOD_TIME << 5, roles, customClaimMap);
        return new RefreshTokenResponse(issueToken, issueRefresh);
    }

    @Override
    public String generateToken() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("No login user");
        }
        String userId = authentication.getName();
        HzbUser user = accountUserService.getUserByUsername(userId);
        List<String> roles = user != null ? Arrays.asList(user.getRole().split(",")) : Collections.emptyList();
        // Generate a long-lived token (no expiration)
        return JwtTokenUtil.issueJwt(userId, null, roles, null);
    }
}
