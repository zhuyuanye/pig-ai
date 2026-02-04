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

package com.pig4cloud.pig.common.warehouse.listener;

import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.warehouse.store.history.AbstractHistoryDataStorage;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * WareHouseApplicationReadyListener
 */
@Slf4j
@Component
public class WareHouseApplicationReadyListener {

    private final Optional<AbstractHistoryDataStorage> historyDataStorage;

    public WareHouseApplicationReadyListener(Optional<AbstractHistoryDataStorage> historyDataStorage) {
        this.historyDataStorage = historyDataStorage;
    }

    @EventListener(classes = {ApplicationReadyEvent.class})
    public void listen() {
        if (historyDataStorage.isEmpty()) {
            log.warn("The historical data repository is not configured");
        }
    }
}
