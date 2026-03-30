package com.pig4cloud.pig.common.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.alerter.SingleAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单条告警 Mapper
 *
 * @author pig4cloud
 */
@Mapper
public interface SingleAlertMapper extends BaseMapper<SingleAlert> {

	/**
	 * 批量更新告警状态
	 * @param status 目标状态
	 * @param ids 告警ID列表
	 * @return 更新行数
	 */
	int updateStatusByIds(@Param("status") String status, @Param("ids") List<Long> ids);

}
