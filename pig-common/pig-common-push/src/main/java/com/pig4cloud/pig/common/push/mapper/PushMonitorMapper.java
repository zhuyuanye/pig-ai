package com.pig4cloud.pig.common.push.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.manager.Monitor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PushMonitorMapper extends BaseMapper<Monitor> {
}
