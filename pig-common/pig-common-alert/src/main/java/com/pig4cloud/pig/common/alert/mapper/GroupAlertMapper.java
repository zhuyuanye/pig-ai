package com.pig4cloud.pig.common.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.alerter.GroupAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 告警分组 Mapper
 *
 * @author pig4cloud
 */
@Mapper
public interface GroupAlertMapper extends BaseMapper<GroupAlert> {

	/**
	 * 批量更新告警组状态
	 * @param status 目标状态
	 * @param ids 告警组ID列表
	 * @return 更新行数
	 */
	int updateStatusByIds(@Param("status") String status, @Param("ids") List<Long> ids);

}
