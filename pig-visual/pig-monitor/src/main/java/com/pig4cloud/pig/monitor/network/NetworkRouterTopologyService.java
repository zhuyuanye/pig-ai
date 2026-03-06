package com.pig4cloud.pig.monitor.network;

import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 路由器拓扑服务
 */
public interface NetworkRouterTopologyService {

    /**
     * 拓扑路由器 防火墙 三层交换机 连接设备
     * @param config
     * @return
     * @throws IOException
     */
     List<Map<String, String>> discover(SnmpConfigInfo config)throws IOException;

}
