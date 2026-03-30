package com.pig4cloud.pig.common.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.manager.Monitor;
import org.apache.ibatis.annotations.Mapper;

/**
 * 告警模块-监控任务 Mapper
 *
 * @author pig4cloud
 */
@Mapper
public interface AlertMonitorMapper extends BaseMapper<Monitor> {

}
