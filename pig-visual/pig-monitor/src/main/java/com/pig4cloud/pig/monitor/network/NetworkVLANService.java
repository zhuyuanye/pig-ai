package com.pig4cloud.pig.monitor.network;

import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * VLAN 拓扑服务
 */
public interface NetworkVLANService {

    /**
     * 获取VLAN拓扑信息
     * @param config
     * @return
     * @throws IOException
     */
    List<Map<String, Object>> discover(SnmpConfigInfo config) throws IOException;
}
