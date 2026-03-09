package com.pig4cloud.pig.monitor.service;

import com.pig4cloud.pig.common.core.entity.dto.ValuePlainDTO;
import com.pig4cloud.pig.common.core.entity.manager.Monitor;

import java.util.List;

public interface MonitorMetricsService {

    List<ValuePlainDTO> metricsValueList(Monitor monitor, String metrics, String metric,
                                         String label, String history, Boolean interval);
}
