package com.pig4cloud.pig.common.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.alerter.AlertDefine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 告警规则定义 Mapper
 *
 * @author pig4cloud
 */
@Mapper
public interface AlertDefineMapper extends BaseMapper<AlertDefine> {

	/**
	 * 根据类型查询已启用的告警规则
	 * @param type 告警类型
	 * @return 告警规则列表
	 */
	List<AlertDefine> selectByTypeAndEnableTrue(@Param("type") String type);

}
