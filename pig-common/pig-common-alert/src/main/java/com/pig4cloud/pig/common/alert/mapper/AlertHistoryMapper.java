package com.pig4cloud.pig.common.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.alerter.AlertHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 告警历史记录 Mapper
 *
 * @author pig4cloud
 */
@Mapper
public interface AlertHistoryMapper extends BaseMapper<AlertHistory> {

	/**
	 * 根据时间范围删除告警历史记录
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 删除行数
	 */
	int deleteByCreateAlertTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
