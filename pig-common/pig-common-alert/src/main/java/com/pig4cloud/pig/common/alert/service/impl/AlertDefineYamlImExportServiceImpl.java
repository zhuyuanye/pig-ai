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

package com.pig4cloud.pig.common.alert.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.alert.dto.ExportAlertDefineDTO;
import com.pig4cloud.pig.common.core.util.export.YamlExportUtils;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.pig4cloud.pig.common.core.constants.ExportFileConstants.YamlFile.FILE_SUFFIX;
import static com.pig4cloud.pig.common.core.constants.ExportFileConstants.YamlFile.TYPE;

/**
 * Configure the import and export Yaml format.
 */

@Slf4j
@Service
public class AlertDefineYamlImExportServiceImpl extends AlertDefineAbstractImExportServiceImpl {

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public String getFileName() {
        return fileNamePrefix() + FILE_SUFFIX;
    }

    @Override
    public List<ExportAlertDefineDTO> parseImport(InputStream is) {
        Yaml yaml = new Yaml();
        return yaml.load(is);
    }

    @Override
    public void writeOs(List<ExportAlertDefineDTO> exportAlertDefineList, OutputStream os) {

        YamlExportUtils.exportWriteOs(exportAlertDefineList, os);
    }

}
