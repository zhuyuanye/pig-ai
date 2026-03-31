package com.pig4cloud.pig.monitor.network.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.network.NetworkTopologyInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网络拓扑信息 Mapper 接口
 */
@Mapper
public interface NetworkTopologyMapper extends BaseMapper<NetworkTopologyInfo> {

}
