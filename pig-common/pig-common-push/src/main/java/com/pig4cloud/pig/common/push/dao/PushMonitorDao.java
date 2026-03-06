/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.push.dao;

import com.pig4cloud.pig.common.core.entity.manager.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * push monitor dao
 */
public interface PushMonitorDao extends JpaRepository<Monitor, Long> {

    /**
     * Find all monitoring entities by type
     * @param type Monitoring type
     * @return Monitoring entity list
     */
    List<Monitor> findMonitorsByType(byte type);

}