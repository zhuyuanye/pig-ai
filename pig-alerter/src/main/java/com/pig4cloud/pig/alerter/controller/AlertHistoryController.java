package com.pig4cloud.pig.alerter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.pig4cloud.pig.common.alert.notice.AlertHistoryService;
import com.pig4cloud.pig.common.core.entity.alerter.AlertHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 历史告警查询控制器
 * <p>提供告警历史数据的查询和删除能力，支持按时间范围筛选</p>
 *
 * @author pig4cloud
 */
@RestController
@RequestMapping("/api/alertHistory")
@Tag(name = "告警历史接口", description = "提供报警历史数据的查询能力")
public class AlertHistoryController {

    @Autowired
    private AlertHistoryService alertHistoryService;

    /**
     * 查询报警历史记录（支持时间范围，可选）
     *
     * @param startTime 开始时间，格式：yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间，格式：yyyy-MM-dd HH:mm:ss
     * @return 报警历史记录列表
     */
    @GetMapping("/query")
    @Operation(
            summary = "查询报警历史记录",
            description = "可根据时间范围（startTime 和 endTime）筛选报警历史记录。如果不传，则查询全部记录。"
    )
    public List<AlertHistory> queryAlertHistory(
            @Parameter(
                    description = "开始时间，格式：yyyy-MM-dd HH:mm:ss，例：2025-06-01 00:00:00",
                    example = "2025-06-01 00:00:00"
            )
            @RequestParam(value = "startTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,

            @Parameter(
                    description = "结束时间，格式：yyyy-MM-dd HH:mm:ss，例：2025-06-21 23:59:59",
                    example = "2025-06-21 23:59:59"
            )
            @RequestParam(value = "endTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return alertHistoryService.getAlertHistoryByOptionalTime(startTime, endTime);
    }


    /**
     * 删除指定时间范围的报警历史记录
     *
     * @param startTime 开始时间，格式：yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间，格式：yyyy-MM-dd HH:mm:ss
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    @Operation(
            summary = "删除报警历史记录",
            description = "根据时间范围（startTime 和 endTime）删除报警历史记录。必须同时传入开始和结束时间。"
    )
    public ResponseEntity<String> deleteAlertHistory(
            @Parameter(
                    description = "开始时间，格式：yyyy-MM-dd HH:mm:ss",
                    example = "2025-06-01 00:00:00"
            )
            @RequestParam("startTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,

            @Parameter(
                    description = "结束时间，格式：yyyy-MM-dd HH:mm:ss",
                    example = "2025-06-21 23:59:59"
            )
            @RequestParam("endTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        alertHistoryService.deleteByCreateAlertTimeBetween(startTime, endTime);
        return ResponseEntity.ok("删除成功");
    }

    /**
     * 删除指定时间范围的报警历史记录（支持动态表名）
     *
     * @param tableName 表名，例如 alert_history_2025
     * @param startTime 开始时间，格式：yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间，格式：yyyy-MM-dd HH:mm:ss
     * @return 操作结果
     */
    @DeleteMapping("/deleteByTable")
    @Operation(
            summary = "删除报警历史记录（指定表）",
            description = "根据表名和时间范围（startTime 和 endTime）删除报警历史记录。必须传入合法表名以及开始和结束时间。"
    )
    public ResponseEntity<String> deleteAlertHistoryByTable(
            @Parameter(
                    description = "表名，例如 alert_history_2025",
                    example = "alert_history_2025"
            )
            @RequestParam("tableName") String tableName,

            @Parameter(
                    description = "开始时间，格式：yyyy-MM-dd HH:mm:ss",
                    example = "2025-06-01 00:00:00"
            )
            @RequestParam("startTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,

            @Parameter(
                    description = "结束时间，格式：yyyy-MM-dd HH:mm:ss",
                    example = "2025-06-30 23:59:59"
            )
            @RequestParam("endTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        alertHistoryService.deleteByTimeRangeFromDynamicTable(tableName, startTime, endTime);
        return ResponseEntity.ok("指定表中数据删除成功");
    }

}
