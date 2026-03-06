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
import com.usthe.sureness.provider.DefaultAccount;
import com.usthe.sureness.provider.SurenessAccount;
import com.usthe.sureness.provider.SurenessAccountProvider;
import com.usthe.sureness.provider.ducument.DocumentAccountProvider;
import com.usthe.sureness.subject.SubjectSum;
import com.usthe.sureness.util.JsonWebTokenUtil;
import com.usthe.sureness.util.Md5Util;
import com.usthe.sureness.util.SurenessContextHolder;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.pig4cloud.pig.common.core.entity.user.HzbUser;
import com.pig4cloud.pig.common.core.util.JsonUtil;
import com.pig4cloud.pig.monitor.pojo.dto.LoginDto;
import com.pig4cloud.pig.monitor.pojo.dto.RefreshTokenResponse;
import com.pig4cloud.pig.monitor.service.AccountService;
import com.pig4cloud.pig.monitor.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Account service
 */
@Service
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AccountServiceImpl implements AccountService {


    @Autowired
    private AccountUserService accountUserService;

    /**
     * Token validity time in seconds
     */
    private static final long PERIOD_TIME = 3600L;
    /**
     * account data provider
     */
    private final SurenessAccountProvider accountProvider = new DocumentAccountProvider();

    @Override
    public Map<String, String> authGetToken(LoginDto loginDto) throws AuthenticationException {
        //修改登录
        SurenessAccount account = null;
        HzbUser user = accountUserService.getUserByUsername(loginDto.getIdentifier());
        log.info("authGetToken:user:{}", JSON.toJSONString(user));
        if (user == null || StringUtils.isBlank(user.getPassword())) {
            log.error("getUserByUsername：user == null || password is blank");
            throw new AuthenticationException("Incorrect Account or Password");
        } else {
            DefaultAccount.Builder builder = new DefaultAccount.Builder(user.getUsername());
            builder.setPassword(user.getPassword());
            builder.setOwnRoles(Arrays.asList(user.getRole().split(",")));
            builder.setDisabledAccount(Boolean.FALSE)
                .setExcessiveAttempts(Boolean.FALSE);
            account = builder.build();

            String password = loginDto.getCredential();
            if (StringUtils.isNotBlank(account.getSalt())) {
                password = Md5Util.md5(password + account.getSalt());
            }
            if (!user.getPassword().equals(password)) {
                log.error("getUserByUsername：password invalid");
                throw new AuthenticationException("Incorrect Account or Password");
            }
            if (account.isDisabledAccount() || account.isExcessiveAttempts()) {
                log.error("getUserByUsername：Expired or Illegal Account");
                throw new AuthenticationException("Expired or Illegal Account");
            }
        }
        // Get the roles the user has - rbac
        List<String> roles = account.getOwnRoles();
        // Issue TOKEN
        String issueToken = JsonWebTokenUtil.issueJwt(loginDto.getIdentifier(), PERIOD_TIME, roles);
        Map<String, Object> customClaimMap = new HashMap<>(1);
        customClaimMap.put("refresh", true);
        String issueRefresh = JsonWebTokenUtil.issueJwt(loginDto.getIdentifier(), PERIOD_TIME << 5, customClaimMap);
        Map<String, String> resp = new HashMap<>(2);
        resp.put("token", issueToken);
        resp.put("refreshToken", issueRefresh);
        resp.put("role", JsonUtil.toJson(roles));

        return resp;
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) throws Exception {
        Claims claims = JsonWebTokenUtil.parseJwt(refreshToken);
        String userId = String.valueOf(claims.getSubject());
        boolean isRefresh = claims.get("refresh", Boolean.class);
        if (StringUtils.isBlank(userId) || !isRefresh) {
            throw new AuthenticationException("Illegal Refresh Token");
        }
        SurenessAccount account = accountProvider.loadAccount(userId);
        if (account == null) {
            throw new AuthenticationException("Not Exists This Token Mapping Account");
        }
        List<String> roles = account.getOwnRoles();
        String issueToken = issueToken(userId, roles, PERIOD_TIME);
        String issueRefresh = issueToken(userId, roles, PERIOD_TIME << 5);
        return new RefreshTokenResponse(issueToken, issueRefresh);
    }

    @Override
    public String generateToken() throws AuthenticationException {
        SubjectSum subjectSum = SurenessContextHolder.getBindSubject();
        String userId = String.valueOf(subjectSum.getPrincipal());
        SurenessAccount account = accountProvider.loadAccount(userId);
        if (account == null) {
            throw new AuthenticationException("Not Exists This Token Mapping Account");
        }
        List<String> roles = account.getOwnRoles();
        return issueToken(userId, roles, null);
    }

    private String issueToken(String userId, List<String> roles, Long expirationMillis) {
        Map<String, Object> customClaimMap = new HashMap<>(1);
        customClaimMap.put("refresh", true);
        return JsonWebTokenUtil.issueJwt(userId, expirationMillis, roles, customClaimMap);
    }

    private SurenessAccount getUser(LoginDto loginDto) {


        DefaultAccount.Builder builder = new DefaultAccount.Builder("");




        return null;
    }



}
