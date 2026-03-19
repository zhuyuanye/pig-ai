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

package com.pig4cloud.pig.monitor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.monitor.util.JwtTokenUtil;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import com.pig4cloud.pig.common.base.dao.GeneralConfigDao;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.entity.manager.GeneralConfig;
import com.pig4cloud.pig.common.core.util.TimeZoneUtil;
import com.pig4cloud.pig.monitor.pojo.dto.MuteConfig;
import com.pig4cloud.pig.monitor.pojo.dto.SystemConfig;
import com.pig4cloud.pig.monitor.pojo.dto.SystemSecret;
import com.pig4cloud.pig.monitor.pojo.dto.TemplateConfig;
import com.pig4cloud.pig.monitor.service.AppService;
import com.pig4cloud.pig.monitor.service.impl.MuteGeneralConfigServiceImpl;
import com.pig4cloud.pig.monitor.service.impl.SystemGeneralConfigServiceImpl;
import com.pig4cloud.pig.monitor.service.impl.SystemSecretServiceImpl;
import com.pig4cloud.pig.monitor.service.impl.TemplateConfigServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * Config Initializer
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE + 2)
public class ConfigInitializer implements SmartLifecycle {

    private boolean running = false;

    private static final String DEFAULT_JWT_SECRET = "CyaFv0bwq2Eik0jdrKUtsA6bx3sDJeFV643R "
            + "LnfKefTjsIfJLBa2YkhEqEGtcHDTNe4CU6+9 "
            + "8tVt4bisXQ13rbN0oxhUZR73M6EByXIO+SV5 "
            + "dKhaX0csgOCTlCxq20yhmUea6H6JIpSE2Rwp";

    @Value("${monitor.jwt.secret:" + DEFAULT_JWT_SECRET + "}")
    private String currentJwtSecret;

    @Resource
    private SystemGeneralConfigServiceImpl systemGeneralConfigService;

    @Resource
    private SystemSecretServiceImpl systemSecretService;

    @Resource
    private TemplateConfigServiceImpl templateConfigService;

    @Resource
    private MuteGeneralConfigServiceImpl muteGeneralConfigService;

    @Resource
    private AppService appService;

    @Resource
    protected GeneralConfigDao generalConfigDao;

    @Resource
    protected ObjectMapper objectMapper;

    @SneakyThrows
    public void initConfig() {
        // for system config
        SystemConfig systemConfig = systemGeneralConfigService.getConfig();
        if (systemConfig != null) {
            TimeZoneUtil.setTimeZoneAndLocale(systemConfig.getTimeZoneId(), systemConfig.getLocale());

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            objectMapper.setTimeZone(TimeZone.getDefault())
                    .setDateFormat(simpleDateFormat);
        } else {
            // init system config data
            systemConfig = SystemConfig.builder().timeZoneId(TimeZone.getDefault().getID()).theme("default")
                                   .locale(Locale.getDefault().getLanguage() + CommonConstants.LOCALE_SEPARATOR
                                                   + Locale.getDefault().getCountry())
                                   .build();
            String contentJson = objectMapper.writeValueAsString(systemConfig);
            GeneralConfig generalConfig2Save = GeneralConfig.builder()
                                                       .type(systemGeneralConfigService.type())
                                                       .content(contentJson)
                                                       .build();
            generalConfigDao.save(generalConfig2Save);
        }
        // for template config, flush the template config in db to memory
        TemplateConfig templateConfig = templateConfigService.getConfig();
        appService.updateCustomTemplateConfig(templateConfig);
        // for system secrets
        if (DEFAULT_JWT_SECRET.equals(currentJwtSecret)) {
            // use the random jwt secret
            SystemSecret systemSecret = systemSecretService.getConfig();
            if (systemSecret == null || StringUtils.isBlank(systemSecret.getJwtSecret())) {
                char[] chars = DEFAULT_JWT_SECRET.toCharArray();
                Random rand = new Random();
                for (int i = 0; i < chars.length; i++) {
                    int index = rand.nextInt(chars.length);
                    char temp = chars[i];
                    chars[i] = chars[index];
                    chars[index] = temp;
                }
                currentJwtSecret = new String(chars);
                systemSecret = SystemSecret.builder().jwtSecret(currentJwtSecret).build();
                systemSecretService.saveConfig(systemSecret);
            } else {
                currentJwtSecret = systemSecret.getJwtSecret();
            }
        }
        // else use the user custom jwt secret
        // set the jwt secret token in util
        JwtTokenUtil.setDefaultSecretKey(currentJwtSecret);

        // init web-app mute config
        MuteConfig muteConfig = muteGeneralConfigService.getConfig();
        if (muteConfig == null) {
            muteConfig = MuteConfig.builder().mute(true).build();
            muteGeneralConfigService.saveConfig(muteConfig);
        }
    }

    @Override
    public void start() {
        initConfig();
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
