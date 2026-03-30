/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.pig4cloud.pig.monitor.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.base.mapper.GeneralConfigMapper;
import com.pig4cloud.pig.common.core.constants.GeneralConfigTypeEnum;
import com.pig4cloud.pig.monitor.pojo.dto.MuteConfig;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * mute config service
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
@Slf4j
public class MuteGeneralConfigServiceImpl extends AbstractGeneralConfigServiceImpl<MuteConfig> {

    /**
     * <p>Constructor, passing in GeneralConfigDao, ObjectMapper and type.</p>
     *
     * @param generalConfigDao Dao object
     * @param objectMapper     JSON tool object
     */
    protected MuteGeneralConfigServiceImpl(GeneralConfigMapper generalConfigMapper, ObjectMapper objectMapper) {
        super(generalConfigMapper, objectMapper);
    }

    /**
     * <p>Get TypeReference object of configuration type.</p>
     *
     * @return TypeReference object
     */
    @Override
    public TypeReference<MuteConfig> getTypeReference() {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return MuteConfig.class;
            }
        };
    }

    /**
     * config type: email, sms
     *
     * @return type string
     */
    @Override
    public String type() {
        return GeneralConfigTypeEnum.mute.name();
    }

    /**
     * handler after save config
     *
     * @param config config
     */
    @Override
    public void handler(MuteConfig config) {
        log.info("handler mute config: {}", config);
    }
}
