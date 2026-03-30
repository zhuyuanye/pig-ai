package com.pig4cloud.pig.common.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.manager.Collector;
import org.apache.ibatis.annotations.Mapper;

/**
 * 告警模块-采集器 Mapper
 *
 * @author pig4cloud
 */
@Mapper
public interface AlertCollectorMapper extends BaseMapper<Collector> {

}
