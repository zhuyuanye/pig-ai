package com.pig4cloud.pig.monitor.network;

import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 交换机网络拓扑
 */
public interface NetworkSwitchTopologyDiscoverService {

    /**
     * 获取交换机MAC表
     * @param config SNMP 配置参数
     * @return 交换机MAC表
     * @throws IOException 获取交换机MAC表异常
     */
     List<Map<String, String>> discoverSwitchMacTable(SnmpConfigInfo config) throws IOException;

}
