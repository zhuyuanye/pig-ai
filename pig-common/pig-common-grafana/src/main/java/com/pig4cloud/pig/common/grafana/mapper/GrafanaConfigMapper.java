package com.pig4cloud.pig.common.grafana.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.manager.GeneralConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GrafanaConfigMapper extends BaseMapper<GeneralConfig> {
}
