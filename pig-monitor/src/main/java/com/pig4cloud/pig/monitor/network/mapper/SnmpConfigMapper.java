package com.pig4cloud.pig.monitor.network.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * SNMP 配置信息 Mapper 接口
 */
@Mapper
public interface SnmpConfigMapper extends BaseMapper<SnmpConfigInfo> {

}
