package com.pig4cloud.pig.common.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.alerter.AlertDefineMonitorBind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 告警定义与监控绑定关系 Mapper
 *
 * @author pig4cloud
 */
@Mapper
public interface AlertDefineBindMapper extends BaseMapper<AlertDefineMonitorBind> {

	/**
	 * 根据告警定义ID删除绑定关系
	 * @param alertDefineId 告警定义ID
	 * @return 删除行数
	 */
	int deleteByAlertDefineId(@Param("alertDefineId") Long alertDefineId);

	/**
	 * 根据监控ID删除绑定关系
	 * @param monitorId 监控ID
	 * @return 删除行数
	 */
	int deleteByMonitorId(@Param("monitorId") Long monitorId);

	/**
	 * 根据监控ID列表批量删除绑定关系
	 * @param monitorIds 监控ID列表
	 * @return 删除行数
	 */
	int deleteByMonitorIds(@Param("monitorIds") Set<Long> monitorIds);

}
