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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.common.alert.dto.MailServerConfig;
import com.pig4cloud.pig.common.base.mapper.GeneralConfigMapper;
import com.pig4cloud.pig.common.core.constants.GeneralConfigTypeEnum;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * MailGeneralConfigServiceImpl class is the implementation of general email configuration service,
 * which inherits the AbstractGeneralConfigServiceImpl class.
 */

@Service
public class MailGeneralConfigServiceImpl extends AbstractGeneralConfigServiceImpl<MailServerConfig> {

    /**
     * MailGeneralConfigServiceImpl's constructor creates an instance of this class
     * through the default constructor or deserialization construction (setBeanProps).
     * The parameter generalConfigDao is used for dao layer operation data,
     * and objectMapper is used for object mapping.
     * @param generalConfigDao dao layer operation data, needed to create an instance of this class
     * @param objectMapper     object mapping , needed to create an instance of this class
     */
    public MailGeneralConfigServiceImpl(GeneralConfigMapper generalConfigMapper, ObjectMapper objectMapper) {
        super(generalConfigMapper, objectMapper);
    }

    @Override
    public String type() {
        return GeneralConfigTypeEnum.email.name();
    }

    /**
     * This method is used to get the TypeReference of NoticeSender type for subsequent processing.
     * a TypeReference of NoticeSender type
     */
    @Override
    public TypeReference<MailServerConfig> getTypeReference() {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return MailServerConfig.class;
            }
        };
    }
}
