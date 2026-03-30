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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.pig.common.base.mapper.GeneralConfigMapper;
import com.pig4cloud.pig.common.base.service.GeneralConfigService;
import com.pig4cloud.pig.common.core.entity.manager.GeneralConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Abstract implementation of GeneralConfigService, providing CRUD operations for configurations.</p>
 */
@Slf4j
abstract class AbstractGeneralConfigServiceImpl<T> implements GeneralConfigService<T> {

    protected final GeneralConfigMapper generalConfigMapper;

    protected final ObjectMapper objectMapper;

    /**
     * <p>Constructor, passing in GeneralConfigMapper, ObjectMapper and type.</p>
     * @param generalConfigMapper Mapper object
     * @param objectMapper     JSON tool object
     */
    protected AbstractGeneralConfigServiceImpl(GeneralConfigMapper generalConfigMapper, ObjectMapper objectMapper) {
        this.generalConfigMapper = generalConfigMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * <p>Save a configuration.</p>
     * @param config need to save configuration object
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveConfig(T config) {
        try {
            String contentJson = objectMapper.writeValueAsString(config);

            GeneralConfig generalConfig2Save = GeneralConfig.builder()
                    .type(type())
                    .content(contentJson)
                    .build();
            generalConfigMapper.insert(generalConfig2Save);
            log.info("Configuration saved successfully");
            handler(getConfig());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Configuration saved failed: " + e.getMessage());
        }
    }

    /**
     * <p>Get a configuration.</p>
     * @return query configuration object
     */
    @Override
    public T getConfig() {
        GeneralConfig generalConfig = generalConfigMapper.selectOne(new LambdaQueryWrapper<GeneralConfig>().eq(GeneralConfig::getType, type()));
        if (generalConfig == null) {
            return null;
        }
        try {
            return objectMapper.readValue(generalConfig.getContent(), getTypeReference());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Get configuration failed: " + e.getMessage());
        }
    }

    /**
     * <p>Get TypeReference object of configuration type.</p>
     * @return TypeReference object
     */
    protected abstract TypeReference<T> getTypeReference();

}
