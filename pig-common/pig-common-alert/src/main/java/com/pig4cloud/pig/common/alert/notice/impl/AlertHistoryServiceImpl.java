package com.pig4cloud.pig.common.alert.notice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.pig.common.alert.mapper.AlertHistoryMapper;
import com.pig4cloud.pig.common.alert.notice.AlertHistoryService;
import com.pig4cloud.pig.common.core.entity.alerter.AlertHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AlertHistoryServiceImpl implements AlertHistoryService {

    @Resource
    private AlertHistoryMapper alertHistoryMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 根据时间范围查询历史数据
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 历史报警列表
     */
    @Override
    public List<AlertHistory> getAlertHistoryByOptionalTime(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return alertHistoryMapper.selectList(
                    new LambdaQueryWrapper<AlertHistory>()
                            .between(AlertHistory::getCreateAlertTime, start, end));
        } else {
            return alertHistoryMapper.selectList(null);
        }
    }
    @Transactional
    @Override
    public void deleteByCreateAlertTimeBetween(LocalDateTime start, LocalDateTime end) {
        alertHistoryMapper.deleteByCreateAlertTimeBetween(start, end);
    }


    /**
     * 动态表名删除告警历史记录
     * @param tableName 表名
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    @Transactional
    @Override
    public void deleteByTimeRangeFromDynamicTable(String tableName, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = String.format("DELETE FROM %s WHERE create_alert_time BETWEEN ? AND ?", tableName);
        jdbcTemplate.update(sql, formatter.format(startTime), formatter.format(endTime));
    }
}
