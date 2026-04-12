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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.pig4cloud.pig.common.core.entity.dto.Message;
import com.pig4cloud.pig.common.core.entity.dto.PluginUpload;
import com.pig4cloud.pig.common.core.entity.manager.PluginMetadata;
import com.pig4cloud.pig.monitor.pojo.dto.PluginParam;
import com.pig4cloud.pig.monitor.pojo.dto.PluginParametersVO;
import com.pig4cloud.pig.monitor.service.PluginService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * plugin management API
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Plugin Manage API")
@RestController
@RequestMapping(path = "/api/plugin", produces = {APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class PluginController {

    private final PluginService pluginService;

    @PostMapping
    @Operation(summary = "upload plugin", description = "upload plugin")
    public ResponseEntity<Message<Void>> uploadNewPlugin(@Valid PluginUpload pluginUpload) {
        pluginService.savePlugin(pluginUpload);
        return ResponseEntity.ok(Message.success("Add success"));
    }


    @GetMapping()
    @Operation(summary = "Get Plugins information", description = "Obtain plugins information based on conditions")
    public ResponseEntity<Message<IPage<PluginMetadata>>> getPlugins(
        @Parameter(description = "plugin name search", example = "status") @RequestParam(required = false) String search,
        @Parameter(description = "List current page", example = "0") @RequestParam(defaultValue = "0") int pageIndex,
        @Parameter(description = "Number of list pagination", example = "8") @RequestParam(defaultValue = "8") int pageSize) {
        IPage<PluginMetadata> alertPage = pluginService.getPlugins(search, pageIndex, pageSize);
        return ResponseEntity.ok(Message.success(alertPage));
    }

    @DeleteMapping()
    @Operation(summary = "Delete plugins based on ID", description = "Delete plugins based on ID")
    public ResponseEntity<Message<Void>> deletePlugins(
        @Parameter(description = "Plugin IDs ", example = "6565463543") @RequestParam(required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            pluginService.deletePlugins(new HashSet<>(ids));
        }
        return ResponseEntity.ok(Message.success("Delete success"));
    }


    @PutMapping()
    @Operation(summary = "Update enable status", description = "Delete plugins based on ID")
    public ResponseEntity<Message<Void>> updatePluginStatus(@RequestBody PluginMetadata plugin) {
        pluginService.updateStatus(plugin);
        return ResponseEntity.ok(Message.success("Update success"));
    }

    @GetMapping("/params/define")
    @Operation(summary = "get param define", description = "get param define by jar path")
    public ResponseEntity<Message<PluginParametersVO>> getParamDefine(@RequestParam Long pluginMetadataId) {
        PluginParametersVO plugins = pluginService.getParamDefine(pluginMetadataId);
        return ResponseEntity.ok(Message.success(plugins));
    }

    @PostMapping("/params")
    @Operation(summary = "get param define", description = "get param define by jar path")
    public ResponseEntity<Message<Boolean>> saveParams(@RequestBody List<PluginParam> pluginParams) {
        pluginService.savePluginParam(pluginParams);
        return ResponseEntity.ok(Message.success(true));
    }

}
