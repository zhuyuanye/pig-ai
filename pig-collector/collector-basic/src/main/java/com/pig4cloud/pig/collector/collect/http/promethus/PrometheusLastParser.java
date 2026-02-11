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

package com.pig4cloud.pig.collector.collect.http.promethus;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.entity.job.protocol.HttpProtocol;
import com.pig4cloud.pig.common.core.entity.message.CollectRep;

import java.util.List;

/**
 * Prometheus Last Parser
 */
@Slf4j
@NoArgsConstructor
public class PrometheusLastParser extends AbstractPrometheusParse {
    @Override
    public Boolean checkType(String responseStr) {
        log.error("prometheus response data:{} ,no adaptive parser", responseStr);
        return true;
    }

    @Override
    public void parse(String resp, List<String> aliasFields, HttpProtocol http, CollectRep.MetricsData.Builder builder) {
        CollectRep.ValueRow.Builder valueRowBuilder = CollectRep.ValueRow.newBuilder();
        aliasFields.forEach(aliasField -> valueRowBuilder.addColumn(CommonConstants.NULL_VALUE));
    }
}
