package com.pig4cloud.pig.monitor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.core.entity.dto.Message;
import com.pig4cloud.pig.common.core.entity.dto.ValuePlainDTO;
import com.pig4cloud.pig.common.core.entity.manager.Monitor;
import com.pig4cloud.pig.monitor.service.MonitorMetricsService;
import com.pig4cloud.pig.monitor.service.MonitorService;
import com.pig4cloud.pig.common.warehouse.service.MetricsDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pig4cloud.pig.common.core.constants.CommonConstants.FAIL_CODE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @ClassName MonitorMetricsController
 * @Author Administrator
 * @Date 2025/06/16 15:14
 * @Description 监控指标数据
 */
@Slf4j
@Tag(name = "Monitor Metrics API")
@RestController
@RequestMapping(path = "/api/monitorMetrics", produces = {APPLICATION_JSON_VALUE})
public class MonitorMetricsController {

    private final MetricsDataService metricsDataService;

    private final MonitorService monitorService;

    private final MonitorMetricsService monitorMetricsService;

    public MonitorMetricsController(MetricsDataService metricsDataService,
                                    MonitorService monitorService, MonitorMetricsService monitorMetricsService) {
        this.metricsDataService = metricsDataService;
        this.monitorService = monitorService;
        this.monitorMetricsService = monitorMetricsService;
    }

    @GetMapping("/{monitorName}/metric/{metrics}")
    @Operation(summary = "Queries historical data for a specified metric for monitoring", description = "Queries historical data for a specified metric under monitoring")
    public ResponseEntity<Message<List<ValuePlainDTO>>> getMetricHistoryData(
        @Parameter(description = "monitor name", example = "redis-xxx")
        @PathVariable String monitorName,
        @Parameter(description = "monitor metrics ", example = "memory")
        @PathVariable() String metrics,
        @Parameter(description = "monitor metric ", example = "used_memory_peak")
        @RequestParam(required = false) String metric,
        @Parameter(description = "label filter, empty by default", example = "disk2")
        @RequestParam(required = false) String label,
        @Parameter(description = "query historical time period, default 6h-6 hours: s-seconds, M-minutes, h-hours, d-days, w-weeks", example = "6h")
        @RequestParam(required = false) String history,
        @Parameter(description = "aggregate data calc. off by default; 4-hour window, query limit >1 week", example = "false")
        @RequestParam(required = false) Boolean interval
    ) {
        if (!metricsDataService.getWarehouseStorageServerStatus()) {
            return ResponseEntity.ok(Message.fail(FAIL_CODE, "time series database not available"));
        }
        Monitor monitor = monitorService.findMonitorByNameEquals(monitorName);
        if (null == monitor) {
            throw new IllegalArgumentException("monitor does not exists: " + monitorName);
        }

        List<ValuePlainDTO> valuePlainDTOS = monitorMetricsService.metricsValueList(monitor, metrics, metric, label, history, interval);
        return ResponseEntity.ok(Message.success(valuePlainDTOS));
    }
}
