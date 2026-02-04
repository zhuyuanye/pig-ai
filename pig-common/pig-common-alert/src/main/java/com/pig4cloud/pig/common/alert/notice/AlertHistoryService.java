package com.pig4cloud.pig.common.alert.notice;

import com.pig4cloud.pig.common.core.entity.alerter.AlertHistory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 历史报警数据 service
 */
public interface AlertHistoryService {

    /**
     * 历史数据查询 API
     * @param start 开始时间
     * @param end  结束时间
     * @return 历史报警数据列表
     */
    List<AlertHistory> getAlertHistoryByOptionalTime(LocalDateTime start, LocalDateTime end);

    /**
     * 删除报警数据
     * @param start 开始时间
     * @param end  结束时间
     */
    void deleteByCreateAlertTimeBetween(LocalDateTime start, LocalDateTime end);


    /**
     * 删除报表历史数据
     * @param tableName
     * @param startTime
     * @param endTime
     */
    void deleteByTimeRangeFromDynamicTable(String tableName, LocalDateTime startTime, LocalDateTime endTime);
}
