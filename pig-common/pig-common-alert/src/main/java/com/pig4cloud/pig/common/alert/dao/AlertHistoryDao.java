package com.pig4cloud.pig.common.alert.dao;

import com.pig4cloud.pig.common.core.entity.alerter.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报警历史记录 DAO 接口
 */
@Repository
public interface AlertHistoryDao extends JpaRepository<AlertHistory, Long>, JpaSpecificationExecutor<AlertHistory> {

    /**
     * 批量根据 fingerprintId 查询报警记录
     */
    List<AlertHistory> findAllByFingerprintId(String fingerprintId);

    /**
     * 时间范围查询
     * @param start 开始时间
     * @param end  结束时间
     * @return 历史报警数据
     */
    List<AlertHistory> findByCreateAlertTimeBetween(LocalDateTime start, LocalDateTime end);
    /**
     * 批量删除报警记录
     * @param start 开始时间
     * @param end  结束时间
     */
    @Modifying
    @Query(value = "DELETE FROM hzb_alert_history WHERE create_alert_time >= ?1 AND create_alert_time <= ?2", nativeQuery = true)
    void deleteByCreateAlertTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
