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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Sets;
import com.pig4cloud.pig.collector.dispatch.DispatchConstants;
import com.pig4cloud.pig.common.alert.mapper.AlertDefineBindMapper;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.constants.ExportFileConstants;
import com.pig4cloud.pig.common.core.constants.NetworkConstants;
import com.pig4cloud.pig.common.core.constants.SignConstants;
import com.pig4cloud.pig.common.core.entity.grafana.GrafanaDashboard;
import com.pig4cloud.pig.common.core.entity.job.Configmap;
import com.pig4cloud.pig.common.core.entity.job.Job;
import com.pig4cloud.pig.common.core.entity.job.Metrics;
import com.pig4cloud.pig.common.core.entity.manager.*;
import com.pig4cloud.pig.common.core.entity.message.CollectRep;
import com.pig4cloud.pig.common.core.support.event.MonitorDeletedEvent;
import com.pig4cloud.pig.common.core.util.*;
import com.pig4cloud.pig.common.grafana.service.DashboardService;
import com.pig4cloud.pig.common.warehouse.service.WarehouseService;
import com.pig4cloud.pig.monitor.config.ManagerSseManager;
import com.pig4cloud.pig.monitor.mapper.*;
import com.pig4cloud.pig.monitor.pojo.dto.AppCount;
import com.pig4cloud.pig.monitor.pojo.dto.MonitorDto;
import com.pig4cloud.pig.monitor.scheduler.CollectJobScheduling;
import com.pig4cloud.pig.monitor.service.AppService;
import com.pig4cloud.pig.monitor.service.ImExportService;
import com.pig4cloud.pig.monitor.service.LabelService;
import com.pig4cloud.pig.monitor.service.MonitorService;
import com.pig4cloud.pig.monitor.support.exception.MonitorDatabaseException;
import com.pig4cloud.pig.monitor.support.exception.MonitorDetectException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Monitoring and management service implementation
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MonitorServiceImpl implements MonitorService {
    public static final String PATTERN_HTTP = "(?i)http://";
    public static final String PATTERN_HTTPS = "(?i)https://";
    private static final Long MONITOR_ID_TMP = 1000000000L;
    private static final byte ALL_MONITOR_STATUS = 9;

    private static final String CONTENT_VALUE = MediaType.APPLICATION_OCTET_STREAM_VALUE + SignConstants.SINGLE_MARK + "charset=" + StandardCharsets.UTF_8;
    private final Map<String, ImExportService> imExportServiceMap = new HashMap<>();
    @Autowired
    private AppService appService;
    @Autowired
    private CollectJobScheduling collectJobScheduling;
    @Autowired
    private MonitorMapper monitorMapper;
    @Autowired
    private ParamMapper paramMapper;
    @Autowired
    private MonitorBindMapper monitorBindMapper;
    @Autowired
    private CollectorMapper collectorMapper;
    @Autowired
    private CollectorMonitorBindMapper collectorMonitorBindMapper;
    @Autowired
    private AlertDefineBindMapper alertDefineBindMapper;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private ManagerSseManager managerSseManager;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private LabelService labelService;

    public MonitorServiceImpl(List<ImExportService> imExportServiceList) {
        imExportServiceList.forEach(it -> imExportServiceMap.put(it.type(), it));
    }

    @Override
    @Transactional(readOnly = true)
    public void detectMonitor(Monitor monitor, List<Param> params, String collector) throws MonitorDetectException {
        if (CommonConstants.SCRAPE_STATIC.equals(monitor.getScrape()) || !StringUtils.hasText(monitor.getScrape())) {
            detectMonitorDirectly(monitor, params, collector);
        } else {
            detectSdMonitor(monitor, params, collector);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMonitor(Monitor monitor, List<Param> params, String collector, GrafanaDashboard grafanaDashboard) throws RuntimeException {
        // Apply for monitor id
        long monitorId = SnowFlakeIdGenerator.generateId();
        Map<String, String> labels = monitor.getLabels();
        if (labels == null) {
            labels = new HashMap<>(8);
            monitor.setLabels(labels);
        }
        List<Label> addLabels = labelService.determineNewLabels(labels.entrySet());

        if (!addLabels.isEmpty()) {
            addLabels.forEach(label -> labelMapper.insert(label));
        }

        // Construct the collection task Job entity
        boolean isStatic = CommonConstants.SCRAPE_STATIC.equals(monitor.getScrape()) || !StringUtils.hasText(monitor.getScrape());
        String app = isStatic ? monitor.getApp() : monitor.getScrape();
        Job appDefine = appService.getAppDefine(app);
        if (!isStatic) {
            appDefine.setSd(true);
        }
        if (CommonConstants.PROMETHEUS.equals(monitor.getApp())) {
            appDefine.setApp(CommonConstants.PROMETHEUS_APP_PREFIX + monitor.getName());
        }
        appDefine.setMonitorId(monitorId);
        appDefine.setDefaultInterval(monitor.getIntervals());
        appDefine.setCyclic(true);
        appDefine.setTimestamp(System.currentTimeMillis());
        Map<String, String> metadata = Map.of(CommonConstants.LABEL_INSTANCE_NAME, monitor.getName(),
                CommonConstants.LABEL_INSTANCE_HOST, monitor.getHost());
        appDefine.setMetadata(metadata);
        appDefine.setLabels(monitor.getLabels());
        appDefine.setAnnotations(monitor.getAnnotations());
        List<Configmap> configmaps = params.stream().map(param -> {
            param.setMonitorId(monitorId);
            return new Configmap(param.getField(), param.getParamValue(), param.getType());
        }).collect(Collectors.toList());
        appDefine.setConfigmap(configmaps);
        long jobId = collector == null ? collectJobScheduling.addAsyncCollectJob(appDefine, null) :
                collectJobScheduling.addAsyncCollectJob(appDefine, collector);
        try {
            detectMonitor(monitor, params, collector);
        } catch (Exception ignored) {}

        try {
            if (collector != null) {
                CollectorMonitorBind collectorMonitorBind = CollectorMonitorBind.builder()
                        .collector(collector)
                        .monitorId(monitorId)
                        .build();
                collectorMonitorBindMapper.insert(collectorMonitorBind);
            }
            monitor.setId(monitorId);
            monitor.setJobId(jobId);
            // create grafana dashboard
            if (monitor.getApp().equals(CommonConstants.PROMETHEUS) && grafanaDashboard != null && grafanaDashboard.isEnabled()) {
                dashboardService.createOrUpdateDashboard(grafanaDashboard.getTemplate(), monitorId);
            }
            monitorMapper.insert(monitor);
            params.forEach(param -> {
                if (param.getId() == null) {
                    paramMapper.insert(param);
                } else {
                    paramMapper.updateById(param);
                }
            });
        } catch (Exception e) {
            log.error("Error while adding monitor: {}", e.getMessage(), e);
            collectJobScheduling.cancelAsyncCollectJob(jobId);
            throw new MonitorDatabaseException(e.getMessage());
        }
    }

    @Override
    public void export(List<Long> ids, String type, HttpServletResponse res) throws Exception {
        var imExportService = imExportServiceMap.get(type);
        if (imExportService == null) {
            throw new IllegalArgumentException("not support export type: " + type);
        }
        var fileName = imExportService.getFileName();
        res.setHeader(HttpHeaders.CONTENT_DISPOSITION, CONTENT_VALUE);
        res.setContentType(CONTENT_VALUE);
        res.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        res.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        imExportService.exportConfig(res.getOutputStream(), ids);
    }

    @Override
    public void importConfig(MultipartFile file) throws Exception {
        var fileName = FileUtil.getFileName(file);
        var type = FileUtil.getFileType(file);
        try {
            if (!imExportServiceMap.containsKey(type)) {
                String errMsg = ExportFileConstants.FILE + " " + fileName + " is not supported.";
                throw new RuntimeException(errMsg);
            }
            var imExportService = imExportServiceMap.get(type);
            imExportService.importConfig(fileName, file.getInputStream());
        } catch (Exception e){
            managerSseManager.broadcastImportTaskFail(fileName, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void validate(MonitorDto monitorDto, Boolean isModify) throws IllegalArgumentException {
        // The request monitoring parameter matches the monitoring parameter definition mapping check
        Monitor monitor = monitorDto.getMonitor();
        monitor.setHost(monitor.getHost().trim());
        monitor.setName(monitor.getName().trim());
        Map<String, Param> paramMap = monitorDto.getParams()
                .stream()
                .peek(param -> {
                    param.setMonitorId(monitor.getId());
                    String value = param.getParamValue() == null ? null : param.getParamValue().trim();
                    param.setParamValue(value);
                })
                .collect(Collectors.toMap(Param::getField, param -> param));
        // Check name uniqueness and can not equal app type
        if (isModify != null) {
            Optional<Job> defineOptional = appService.getAppDefineOption(monitor.getName());
            if (defineOptional.isPresent()) {
                throw new IllegalArgumentException("Monitoring name cannot be the existed monitoring type name!");
            }
            Monitor existMonitor = monitorMapper.selectOne(new QueryWrapper<Monitor>().eq("name", monitor.getName()));
            if (existMonitor != null) {
                if (isModify) {
                    if (!existMonitor.getId().equals(monitor.getId())) {
                        throw new IllegalArgumentException("Monitoring name already exists!");
                    }
                } else {
                    throw new IllegalArgumentException("Monitoring name already exists!");
                }
            }
        }
        // the dispatch collector must exist if pin
        if (StringUtils.hasText(monitorDto.getCollector())) {
            Collector collector = collectorMapper.selectOne(new QueryWrapper<Collector>().eq("name", monitorDto.getCollector()));
            if (collector == null) {
                throw new IllegalArgumentException("The pinned collector does not exist.");
            }
        } else {
            monitorDto.setCollector(null);
        }
        // Parameter definition structure verification
        List<ParamDefine> paramDefines = appService.getAppParamDefines(monitorDto.getMonitor().getApp());
        if (!CollectionUtils.isEmpty(paramDefines)) {
            for (ParamDefine paramDefine : paramDefines) {
                String field = paramDefine.getField();
                Param param = paramMap.get(field);
                if (Boolean.TRUE.equals(paramDefine.getRequired()) && (param == null || param.getParamValue() == null)) {
                    throw new IllegalArgumentException("Params field " + field + " is required.");
                }
                if (param != null && StringUtils.hasText(param.getParamValue())) {
                    switch (paramDefine.getType()) {
                        case "number":
                            double doubleValue;
                            try {
                                doubleValue = Double.parseDouble(param.getParamValue());
                            } catch (Exception e) {
                                throw new IllegalArgumentException("Params field " + field + " type "
                                        + paramDefine.getType() + " is invalid.");
                            }
                            if (paramDefine.getRange() != null) {
                                if (!IntervalExpressionUtil.validNumberIntervalExpress(doubleValue,
                                        paramDefine.getRange())) {
                                    throw new IllegalArgumentException("Params field " + field + " type "
                                            + paramDefine.getType() + " over range " + paramDefine.getRange());
                                }
                            }
                            param.setType(CommonConstants.PARAM_TYPE_NUMBER);
                            break;
                        case "textarea":
                            Short textareaLimit = paramDefine.getLimit();
                            if (textareaLimit != null && param.getParamValue().length() > textareaLimit) {
                                throw new IllegalArgumentException("Params field " + field + " type "
                                        + paramDefine.getType() + " over limit " + param.getParamValue());
                            }
                            break;
                        case "text":
                            Short textLimit = paramDefine.getLimit();
                            if (textLimit != null && param.getParamValue().length() > textLimit) {
                                throw new IllegalArgumentException("Params field " + field + " type "
                                        + paramDefine.getType() + " over limit " + textLimit);
                            }
                            break;
                        case "host":
                            String hostValue = param.getParamValue();
                            if (hostValue.toLowerCase().contains(NetworkConstants.HTTP_HEADER)) {
                                hostValue = hostValue.replaceAll(PATTERN_HTTP, SignConstants.BLANK);
                            }
                            if (hostValue.toLowerCase().contains(NetworkConstants.HTTPS_HEADER)) {
                                hostValue = hostValue.replace(PATTERN_HTTPS, SignConstants.BLANK);
                            }
                            if (!IpDomainUtil.validateIpDomain(hostValue)) {
                                throw new IllegalArgumentException("Params field " + field + " value "
                                        + hostValue + " is invalid host value.");
                            }
                            break;
                        case "password":
                            // The plaintext password needs to be encrypted for transmission and storage
                            String passwordValue = param.getParamValue();
                            if (!AesUtil.isCiphertext(passwordValue)) {
                                passwordValue = AesUtil.aesEncode(passwordValue);
                                param.setParamValue(passwordValue);
                            }
                            param.setType(CommonConstants.PARAM_TYPE_PASSWORD);
                            break;
                        case "boolean":
                            // boolean check
                            String booleanValue = param.getParamValue();
                            if (!"true".equalsIgnoreCase(booleanValue) && !"false".equalsIgnoreCase(booleanValue)) {
                                throw new IllegalArgumentException("Params field " + field + " value "
                                        + booleanValue + " is invalid boolean value.");
                            }
                            break;
                        case "radio":
                            // radio single value check
                            @SuppressWarnings("unchecked")
                            List<ParamDefine.Option> options = (List<ParamDefine.Option>) (List<?>) paramDefine.getOptions();
                            boolean invalid = true;
                            if (options != null) {
                                for (ParamDefine.Option option : options) {
                                    if (param.getParamValue().equalsIgnoreCase(option.getValue())) {
                                        invalid = false;
                                        break;
                                    }
                                }
                            }
                            if (invalid) {
                                throw new IllegalArgumentException("Params field " + field + " value "
                                        + param.getParamValue() + " is invalid option value");
                            }
                            break;
                        case "checkbox":
                            @SuppressWarnings("unchecked")
                            List<ParamDefine.Option> checkboxOptions = (List<ParamDefine.Option>) (List<?>) paramDefine.getOptions();
                            boolean checkboxInvalid = true;
                            if (checkboxOptions != null) {
                                for (ParamDefine.Option option : checkboxOptions) {
                                    if (param.getParamValue().equalsIgnoreCase(option.getValue())) {
                                        checkboxInvalid = false;
                                        break;
                                    }
                                }
                            }
                            if (checkboxInvalid) {
                                throw new IllegalArgumentException("Params field " + field + " value "
                                        + param.getParamValue() + " is invalid checkbox value");
                            }
                            break;
                        case "metrics-field":
                        case "key-value":
                            if (JsonUtil.fromJson(param.getParamValue(), new TypeReference<>() {
                            }) == null) {
                                throw new IllegalArgumentException("Params field " + field + " value "
                                        + param.getParamValue() + " is invalid key-value value");
                            }
                            break;
                        case "array":
                            String[] arrays = param.getParamValue().split(",");
                            if (arrays.length == 0) {
                                throw new IllegalArgumentException("Param field" + field + " value "
                                        + param.getParamValue() + " is invalid arrays value");
                            }
                            if (param.getParamValue().startsWith("[") && param.getParamValue().endsWith("]")) {
                                param.setParamValue(param.getParamValue().substring(1, param.getParamValue().length() - 1));
                            }
                            break;
                        // todo More parameter definitions and actual value format verification
                        default:
                            throw new IllegalArgumentException("ParamDefine type " + paramDefine.getType() + " is invalid.");
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyMonitor(Monitor monitor, List<Param> params, String collector, GrafanaDashboard grafanaDashboard) throws RuntimeException {
        long monitorId = monitor.getId();
        // Check to determine whether the monitor corresponding to the monitor id exists
        Monitor preMonitor = monitorMapper.selectById(monitorId);
        if (preMonitor == null) {
            throw new IllegalArgumentException("The Monitor " + monitorId + " not exists");
        }
        if (!preMonitor.getApp().equals(monitor.getApp())) {
            // The type of monitoring cannot be modified
            throw new IllegalArgumentException("Can not modify monitor's app type");
        }
        Map<String, String> labels = monitor.getLabels();
        if (labels == null) {
            labels = new HashMap<>(8);
            monitor.setLabels(labels);
        }

        List<Label> addLabels = labelService.determineNewLabels(labels.entrySet());

        if (!addLabels.isEmpty()) {
            addLabels.forEach(label -> labelMapper.insert(label));
        }

        boolean isStatic = CommonConstants.SCRAPE_STATIC.equals(monitor.getScrape()) || !StringUtils.hasText(monitor.getScrape());
        if (preMonitor.getStatus() != CommonConstants.MONITOR_PAUSED_CODE) {
            // Construct the collection task Job entity
            String app = isStatic ? monitor.getApp() : monitor.getScrape();
            Job appDefine = appService.getAppDefine(app);
            if (!isStatic) {
                appDefine.setSd(true);
            }
            if (CommonConstants.PROMETHEUS.equals(monitor.getApp())) {
                appDefine.setApp(CommonConstants.PROMETHEUS_APP_PREFIX + monitor.getName());
            }
            appDefine.setId(preMonitor.getJobId());
            appDefine.setMonitorId(monitorId);
            appDefine.setDefaultInterval(monitor.getIntervals());
            appDefine.setCyclic(true);
            appDefine.setTimestamp(System.currentTimeMillis());
            Map<String, String> metadata = Map.of(CommonConstants.LABEL_INSTANCE_NAME, monitor.getName(),
                    CommonConstants.LABEL_INSTANCE_HOST, monitor.getHost());
            appDefine.setMetadata(metadata);
            appDefine.setLabels(monitor.getLabels());
            appDefine.setAnnotations(monitor.getAnnotations());
            List<Configmap> configmaps = params.stream().map(param ->
                    new Configmap(param.getField(), param.getParamValue(), param.getType())).collect(Collectors.toList());
            appDefine.setConfigmap(configmaps);
            long newJobId;
            if (collector == null) {
                newJobId = collectJobScheduling.updateAsyncCollectJob(appDefine);
            } else {
                newJobId = collectJobScheduling.updateAsyncCollectJob(appDefine, collector);
            }
            monitor.setJobId(newJobId);

            // execute only in non paused status
            try {
                detectMonitor(monitor, params, collector);
            } catch (Exception ignored) {}
        }

        // After the update is successfully released, refresh the database
        try {
            collectorMonitorBindMapper.delete(new QueryWrapper<CollectorMonitorBind>().eq("monitor_id", monitorId));
            if (collector != null) {
                CollectorMonitorBind collectorMonitorBind = CollectorMonitorBind.builder()
                        .collector(collector).monitorId(monitorId)
                        .build();
                collectorMonitorBindMapper.insert(collectorMonitorBind);
            }
            // force update gmtUpdate time, due the case: monitor not change, param change. we also think monitor change
            monitor.setGmtUpdate(LocalDateTime.now());
            // update or open grafana dashboard
            if (monitor.getApp().equals(CommonConstants.PROMETHEUS) && grafanaDashboard != null) {
                if (grafanaDashboard.isEnabled()) {
                    dashboardService.createOrUpdateDashboard(grafanaDashboard.getTemplate(), monitorId);
                } else {
                    dashboardService.closeGrafanaDashboard(monitorId);
                }
            }
            monitorMapper.updateById(monitor);
            params.forEach(param -> {
                if (param.getId() == null) {
                    paramMapper.insert(param);
                } else {
                    paramMapper.updateById(param);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // Repository brushing abnormally cancels the previously delivered task
            collectJobScheduling.cancelAsyncCollectJob(monitor.getJobId());
            throw new MonitorDatabaseException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMonitor(long id) throws RuntimeException {
        deleteMonitors(Sets.newHashSet(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMonitors(Set<Long> ids) throws RuntimeException {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        Set<Long> subMonitorIds = monitorBindMapper.selectList(new QueryWrapper<MonitorBind>().in("biz_id", ids))
                .stream().map(MonitorBind::getMonitorId).collect(Collectors.toSet());
        Set<Long> allMonitorIds = new HashSet<>(ids);
        allMonitorIds.addAll(subMonitorIds);
        List<Monitor> monitors = monitorMapper.selectList(new QueryWrapper<Monitor>().in("id", allMonitorIds));
        if (!monitors.isEmpty()) {
            monitors.forEach(monitor -> monitorMapper.deleteById(monitor.getId()));
            paramMapper.delete(new QueryWrapper<Param>().in("monitor_id", ids));
            Set<Long> monitorIds = monitors.stream().map(Monitor::getId).collect(Collectors.toSet());
            alertDefineBindMapper.deleteByMonitorIds(monitorIds);
            monitorBindMapper.delete(new QueryWrapper<MonitorBind>().in("biz_id", monitorIds));
            for (Monitor monitor : monitors) {
                monitorBindMapper.delete(new QueryWrapper<MonitorBind>().eq("monitor_id", monitor.getId()));
                collectorMonitorBindMapper.delete(new QueryWrapper<CollectorMonitorBind>().eq("monitor_id", monitor.getId()));
                collectJobScheduling.cancelAsyncCollectJob(monitor.getJobId());
                applicationContext.publishEvent(new MonitorDeletedEvent(applicationContext, monitor.getId()));
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MonitorDto getMonitorDto(long id) throws RuntimeException {
        Monitor monitor = monitorMapper.selectById(id);
        if (monitor != null) {
            MonitorDto monitorDto = new MonitorDto();
            List<Param> params = paramMapper.selectList(new QueryWrapper<Param>().eq("monitor_id", id));
            monitorDto.setParams(params);
            if (DispatchConstants.PROTOCOL_PROMETHEUS.equalsIgnoreCase(monitor.getApp()) || monitor.getType() == CommonConstants.MONITOR_TYPE_PUSH_AUTO_CREATE) {
                List<CollectRep.MetricsData> metricsDataList = warehouseService.queryMonitorMetricsData(id);
                List<String> metrics = metricsDataList.stream().map(CollectRep.MetricsData::getMetrics).collect(Collectors.toList());
                monitorDto.setMetrics(metrics);
                monitorDto.setGrafanaDashboard(dashboardService.getDashboardByMonitorId(id));
            } else {
                boolean isStatic = CommonConstants.SCRAPE_STATIC.equals(monitor.getScrape()) || !StringUtils.hasText(monitor.getScrape());
                String type = isStatic ? monitor.getApp() : monitor.getScrape();
                Job job = appService.getAppDefine(type);
                List<String> metrics = job.getMetrics().stream()
                        .filter(Metrics::isVisible)
                        .map(Metrics::getName).collect(Collectors.toList());
                monitorDto.setMetrics(metrics);
            }
            monitorDto.setMonitor(monitor);
            CollectorMonitorBind bind = collectorMonitorBindMapper.selectOne(new QueryWrapper<CollectorMonitorBind>().eq("monitor_id", monitor.getId()));
            if (bind != null) {
                monitorDto.setCollector(bind.getCollector());
            }
            return monitorDto;
        } else {
            return null;
        }
    }

    @Override
    public IPage<Monitor> getMonitors(List<Long> monitorIds, String app, String search, Byte status, String sort, String order, int pageIndex, int pageSize, String labels) {
        QueryWrapper<Monitor> queryWrapper = new QueryWrapper<>();

        // AND conditions
        if (!CollectionUtils.isEmpty(monitorIds)) {
            queryWrapper.in("id", monitorIds);
        }
        if (StringUtils.hasText(app)) {
            queryWrapper.eq("app", app);
        }
        if (status != null && status >= 0 && status < ALL_MONITOR_STATUS) {
            queryWrapper.eq("status", status);
        }

        // OR conditions for search
        if (StringUtils.hasText(search)) {
            queryWrapper.and(wrapper -> {
                wrapper.or().like("host", "%" + search + "%")
                       .or().likeRight("name", search.toLowerCase());
                if (CommonUtil.isNumeric(search)) {
                    wrapper.or().eq("id", Long.parseLong(search));
                }
            });
        }

        // OR conditions for labels
        if (StringUtils.hasText(labels)) {
            queryWrapper.and(wrapper -> {
                String[] labelAres = labels.split(",");
                for (String label : labelAres) {
                    String[] labelArr = label.split(":");
                    String labelName = labelArr[0];
                    String labelValue = labelArr.length == 2 ? labelArr[1] : null;
                    if (labelValue == null) {
                        wrapper.or().like("labels", "%" + labelName + "%");
                    } else {
                        String pattern = String.format("%%\"%s\":\"%s\"%%", labelName, labelValue);
                        wrapper.or().like("labels", pattern);
                    }
                }
            });
        }

        // Sorting
        if (StringUtils.hasText(sort)) {
            if ("asc".equalsIgnoreCase(order)) {
                queryWrapper.orderByAsc(sort);
            } else {
                queryWrapper.orderByDesc(sort);
            }
        }

        // Pagination
        Page<Monitor> page = new Page<>(pageIndex, pageSize);
        return monitorMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void cancelManageMonitors(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // Update monitoring status Delete corresponding monitoring periodic task
        // The jobId is not deleted, and the jobId is reused again after the management is started.
        Set<Long> subMonitorIds = monitorBindMapper.selectList(new QueryWrapper<MonitorBind>().in("biz_id", ids))
                .stream().map(MonitorBind::getMonitorId).collect(Collectors.toSet());
        ids.addAll(subMonitorIds);
        List<Monitor> managedMonitors = monitorMapper.selectList(new QueryWrapper<Monitor>().in("id", ids))
                .stream().filter(monitor ->
                        monitor.getStatus() != CommonConstants.MONITOR_PAUSED_CODE)
                .peek(monitor -> monitor.setStatus(CommonConstants.MONITOR_PAUSED_CODE))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(managedMonitors)) {
            for (Monitor monitor : managedMonitors) {
                collectJobScheduling.cancelAsyncCollectJob(monitor.getJobId());
            }
            managedMonitors.forEach(monitor -> monitorMapper.updateById(monitor));
        }
    }

    @Override
    public void enableManageMonitors(Set<Long> ids) {
        // Update monitoring status Add corresponding monitoring periodic task
        Set<Long> subMonitorIds = monitorBindMapper.selectList(new QueryWrapper<MonitorBind>().in("biz_id", ids))
                .stream().map(MonitorBind::getMonitorId).collect(Collectors.toSet());
        ids.addAll(subMonitorIds);
        List<Monitor> unManagedMonitors = monitorMapper.selectList(new QueryWrapper<Monitor>().in("id", ids))
                .stream().filter(monitor ->
                        monitor.getStatus() == CommonConstants.MONITOR_PAUSED_CODE)
                .peek(monitor -> monitor.setStatus(CommonConstants.MONITOR_UP_CODE))
                .collect(Collectors.toList());
        if (unManagedMonitors.isEmpty()) {
            return;
        }

        for (Monitor monitor : unManagedMonitors) {
            // Construct the collection task Job entity
            List<Param> params = paramMapper.selectList(new QueryWrapper<Param>().eq("monitor_id", monitor.getId()));
            boolean isStatic = CommonConstants.SCRAPE_STATIC.equals(monitor.getScrape()) || !StringUtils.hasText(monitor.getScrape());
            String app = isStatic ? monitor.getApp() : monitor.getScrape();
            Job appDefine = appService.getAppDefine(app);
            if (!isStatic) {
                appDefine.setSd(true);
            }
            if (CommonConstants.PROMETHEUS.equals(monitor.getApp())) {
                appDefine.setApp(CommonConstants.PROMETHEUS_APP_PREFIX + monitor.getName());
            }
            appDefine.setMonitorId(monitor.getId());
            appDefine.setDefaultInterval(monitor.getIntervals());
            appDefine.setCyclic(true);
            appDefine.setTimestamp(System.currentTimeMillis());
            Map<String, String> metadata = Map.of(CommonConstants.LABEL_INSTANCE_NAME, monitor.getName(),
                    CommonConstants.LABEL_INSTANCE_HOST, monitor.getHost());
            appDefine.setMetadata(metadata);
            appDefine.setLabels(monitor.getLabels());
            appDefine.setAnnotations(monitor.getAnnotations());
            List<Configmap> configmaps = params.stream().map(param ->
                    new Configmap(param.getField(), param.getParamValue(), param.getType())).collect(Collectors.toList());
            List<ParamDefine> paramDefaultValue = appDefine.getParams().stream()
                    .filter(item -> StringUtils.hasText(item.getDefaultValue()))
                    .toList();
            paramDefaultValue.forEach(defaultVar -> {
                if (configmaps.stream().noneMatch(item -> item.getKey().equals(defaultVar.getField()))) {
                    Configmap configmap = new Configmap(defaultVar.getField(), defaultVar.getDefaultValue(), CommonConstants.TYPE_STRING);
                    configmaps.add(configmap);
                }
            });
            appDefine.setConfigmap(configmaps);

            // Issue collection tasks
            CollectorMonitorBind bind = collectorMonitorBindMapper.selectOne(new QueryWrapper<CollectorMonitorBind>().eq("monitor_id", monitor.getId()));
            String collector = bind != null ? bind.getCollector() : null;
            long newJobId = collectJobScheduling.addAsyncCollectJob(appDefine, collector);
            monitor.setJobId(newJobId);
            applicationContext.publishEvent(new MonitorDeletedEvent(applicationContext, monitor.getId()));
            try {
                detectMonitor(monitor, params, collector);
            } catch (Exception ignored) {
            }
        }
        unManagedMonitors.forEach(monitor -> monitorMapper.updateById(monitor));
    }

    @Override
    public List<AppCount> getAllAppMonitorsCount() {
        List<AppCount> appCounts = monitorMapper.findAppsStatusCount();
        if (CollectionUtils.isEmpty(appCounts)) {
            return null;
        }
        //Statistical category information, calculate the number of corresponding states for each monitor
        Map<String, AppCount> appCountMap = new HashMap<>(appCounts.size());
        for (AppCount item : appCounts) {
            AppCount appCount = appCountMap.getOrDefault(item.getApp(), new AppCount());
            appCount.setApp(item.getApp());
            switch (item.getStatus()) {
                case CommonConstants.MONITOR_UP_CODE ->
                        appCount.setAvailableSize(appCount.getAvailableSize() + item.getSize());
                case CommonConstants.MONITOR_DOWN_CODE ->
                        appCount.setUnAvailableSize(appCount.getUnAvailableSize() + item.getSize());
                case CommonConstants.MONITOR_PAUSED_CODE ->
                        appCount.setUnManageSize(appCount.getUnManageSize() + item.getSize());
                default -> {
                }
            }
            appCountMap.put(item.getApp(), appCount);
        }
        //Traverse the map obtained by statistics and convert it into a List<App Count> result set
        return appCountMap.values().stream().map(item -> {
            item.setSize(item.getAvailableSize() + item.getUnManageSize() + item.getUnAvailableSize());
            try {
                Job job = appService.getAppDefine(item.getApp());
                item.setCategory(job.getCategory());
            } catch (Exception ignored) {
                return null;
            }
            return item;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void updateAppCollectJob(Job job) {
        List<Monitor> monitors = monitorMapper.selectList(new QueryWrapper<Monitor>().eq("app", job.getApp()))
                .stream().filter(monitor -> monitor.getStatus() != CommonConstants.MONITOR_PAUSED_CODE)
                .toList();
        if (monitors.isEmpty()) {
            return;
        }
        List<CollectorMonitorBind> monitorBinds = collectorMonitorBindMapper.selectList(new QueryWrapper<CollectorMonitorBind>().in(
                "monitor_id", monitors.stream().map(Monitor::getId).collect(Collectors.toSet())));
        Map<Long, String> monitorIdCollectorMap = monitorBinds.stream().collect(
                Collectors.toMap(CollectorMonitorBind::getMonitorId, CollectorMonitorBind::getCollector));
        for (Monitor monitor : monitors) {
            try {
                Job appDefine = job.clone();
                if (monitor == null || appDefine == null || monitor.getId() == null || monitor.getJobId() == null) {
                    log.error("update monitor job error when template modify, define | id | jobId is null. continue");
                    continue;
                }
                if (CommonConstants.PROMETHEUS.equals(monitor.getApp())) {
                    appDefine.setApp(CommonConstants.PROMETHEUS_APP_PREFIX + monitor.getName());
                }
                appDefine.setId(monitor.getJobId());
                appDefine.setMonitorId(monitor.getId());
                appDefine.setDefaultInterval(monitor.getIntervals());
                appDefine.setCyclic(true);
                appDefine.setTimestamp(System.currentTimeMillis());
                Map<String, String> metadata = Map.of(CommonConstants.LABEL_INSTANCE_NAME, monitor.getName(),
                        CommonConstants.LABEL_INSTANCE_HOST, monitor.getHost());
                appDefine.setMetadata(metadata);
                appDefine.setLabels(monitor.getLabels());
                appDefine.setAnnotations(monitor.getAnnotations());
                List<Param> params = paramMapper.selectList(new QueryWrapper<Param>().eq("monitor_id", monitor.getId()));
                List<Configmap> configmaps = params.stream().map(param -> new Configmap(param.getField(),
                        param.getParamValue(), param.getType())).collect(Collectors.toList());
                List<ParamDefine> paramDefaultValue = appDefine.getParams().stream()
                        .filter(item -> StringUtils.hasText(item.getDefaultValue()))
                        .toList();
                paramDefaultValue.forEach(defaultVar -> {
                    if (configmaps.stream().noneMatch(item -> item.getKey().equals(defaultVar.getField()))) {
                        Configmap configmap = new Configmap(defaultVar.getField(), defaultVar.getDefaultValue(), (byte) 1);
                        configmaps.add(configmap);
                    }
                });
                appDefine.setConfigmap(configmaps);
                // if is pinned collector
                String collector = monitorIdCollectorMap.get(monitor.getId());
                // Delivering a collection task
                long newJobId = collectJobScheduling.updateAsyncCollectJob(appDefine, collector);
                monitor.setJobId(newJobId);
                monitorMapper.updateById(monitor);
            } catch (Exception e) {
                log.error("update monitor job error when template modify: {}.continue", e.getMessage(), e);
            }
        }
    }

    @Override
    public Monitor getMonitor(Long monitorId) {
        return monitorMapper.selectById(monitorId);
    }

    @Override
    public Monitor findMonitorByNameEquals(String name) {
        return monitorMapper.selectOne(new QueryWrapper<Monitor>().eq("name", name));
    }

    @Override
    public void updateMonitorStatus(Long monitorId, byte status) {
        Monitor monitor = new Monitor();
        monitor.setId(monitorId);
        monitor.setStatus(status);
        monitorMapper.updateById(monitor);
    }

    @Override
    public List<Monitor> getAppMonitors(String app) {
        return monitorMapper.selectList(new QueryWrapper<Monitor>().eq("app", app));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyMonitor(Long id) {
        // Get the source monitor information
        Monitor sourceMonitor = monitorMapper.selectById(id);
        if (sourceMonitor == null) {
            throw new IllegalArgumentException("Monitor not found: " + id);
        }
        // Get the parameters of source monitor
        List<Param> sourceParams = paramMapper.selectList(new QueryWrapper<Param>().eq("monitor_id", id));
        // Create new monitor object
        Monitor newMonitor = new Monitor();
        // Copy basic properties, exclude ID, jobId and status
        BeanUtils.copyProperties(sourceMonitor, newMonitor, "id", "jobId", "status");
        // Set new name
        newMonitor.setName(sourceMonitor.getName() + "_copy");
        // Set initial status
        newMonitor.setStatus(CommonConstants.MONITOR_UP_CODE);
        // Set create and update time
        newMonitor.setGmtCreate(LocalDateTime.now());
        newMonitor.setGmtUpdate(LocalDateTime.now());
        // Copy parameters
        List<Param> newParams = new ArrayList<>();
        if (!sourceParams.isEmpty()) {
            for (Param sourceParam : sourceParams) {
                Param newParam = new Param();
                BeanUtils.copyProperties(sourceParam, newParam, "id");
                newParam.setMonitorId(newMonitor.getId());
                newParams.add(newParam);
            }
        }
        addMonitor(newMonitor, newParams, null, null);
    }


    private void detectSdMonitor(Monitor monitor, List<Param> params, String collector) {
        Long monitorId = monitor.getId();
        if (monitorId == null || monitorId == 0) {
            monitorId = MONITOR_ID_TMP;
        }
        if (monitor.getScrape() == null || params == null || params.isEmpty()) {
            throw new IllegalArgumentException("scrape params is null or empty!");
        }
        Job appDefine = appService.getAppDefine(monitor.getScrape());
        appDefine.setMonitorId(monitorId);
        appDefine.setCyclic(false);
        appDefine.setTimestamp(System.currentTimeMillis());
        Map<String, String> metadata = Map.of(CommonConstants.LABEL_INSTANCE_NAME, monitor.getName());
        appDefine.setMetadata(metadata);
        appDefine.setLabels(monitor.getLabels());
        appDefine.setAnnotations(monitor.getAnnotations());
        List<Configmap> configmaps = params.stream().map(param ->
                new Configmap(param.getField(), param.getParamValue(), param.getType())).collect(Collectors.toList());
        appDefine.setConfigmap(configmaps);
        appDefine.setSd(true);
        List<CollectRep.MetricsData> collectRep;
        if (collector != null) {
            collectRep = collectJobScheduling.collectSyncJobData(appDefine, collector);
        } else {
            collectRep = collectJobScheduling.collectSyncJobData(appDefine);
        }
        monitor.setStatus(CommonConstants.MONITOR_UP_CODE);
        // If the detection result fails, a detection exception is thrown
        if (collectRep == null || collectRep.isEmpty()) {
            monitor.setStatus(CommonConstants.MONITOR_DOWN_CODE);
            throw new MonitorDetectException("Collect Timeout No Response");
        }
        if (collectRep.get(0).getCode() != CollectRep.Code.SUCCESS) {
            monitor.setStatus(CommonConstants.MONITOR_DOWN_CODE);
            throw new MonitorDetectException(collectRep.get(0).getMsg());
        }
        collectRep.forEach(CollectRep.MetricsData::close);
    }

    private void detectMonitorDirectly(Monitor monitor, List<Param> params, String collector) {
        Long monitorId = monitor.getId();
        if (monitorId == null || monitorId == 0) {
            monitorId = MONITOR_ID_TMP;
        }
        Job appDefine = appService.getAppDefine(monitor.getApp());
        if (CommonConstants.PROMETHEUS.equals(monitor.getApp())) {
            appDefine.setApp(CommonConstants.PROMETHEUS_APP_PREFIX + monitor.getName());
        }
        appDefine.setMonitorId(monitorId);
        appDefine.setCyclic(false);
        appDefine.setTimestamp(System.currentTimeMillis());
        Map<String, String> metadata = Map.of(CommonConstants.LABEL_INSTANCE_NAME, monitor.getName(),
                CommonConstants.LABEL_INSTANCE_HOST, monitor.getHost());
        appDefine.setMetadata(metadata);
        appDefine.setLabels(monitor.getLabels());
        appDefine.setAnnotations(monitor.getAnnotations());
        List<Configmap> configmaps = params.stream().map(param ->
                new Configmap(param.getField(), param.getParamValue(), param.getType())).collect(Collectors.toList());
        appDefine.setConfigmap(configmaps);
        // To detect availability, you only need to collect the set of availability metrics with a priority of 0.
        List<Metrics> availableMetrics = appDefine.getMetrics().stream()
                .filter(item -> item.getPriority() == 0).collect(Collectors.toList());
        appDefine.setMetrics(availableMetrics);
        List<CollectRep.MetricsData> collectRep;
        if (collector != null) {
            collectRep = collectJobScheduling.collectSyncJobData(appDefine, collector);
        } else {
            collectRep = collectJobScheduling.collectSyncJobData(appDefine);
        }
        monitor.setStatus(CommonConstants.MONITOR_UP_CODE);

        // If the detection result fails, a detection exception is thrown
        if (collectRep == null || collectRep.isEmpty()) {
            monitor.setStatus(CommonConstants.MONITOR_DOWN_CODE);
            throw new MonitorDetectException("Collect Timeout No Response");
        }
        if (collectRep.get(0).getCode() != CollectRep.Code.SUCCESS) {
            monitor.setStatus(CommonConstants.MONITOR_DOWN_CODE);
            throw new MonitorDetectException(collectRep.get(0).getMsg());
        }
        collectRep.forEach(CollectRep.MetricsData::close);
    }
}
