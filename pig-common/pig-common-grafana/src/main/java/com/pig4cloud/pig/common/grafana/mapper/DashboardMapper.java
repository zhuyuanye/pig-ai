package com.pig4cloud.pig.common.grafana.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.grafana.GrafanaDashboard;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashboardMapper extends BaseMapper<GrafanaDashboard> {
}
