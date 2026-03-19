package com.pig4cloud.pig.monitor.network;

import java.util.Map;

/**
 * 网络设备服务接口
 */
public interface NetworkEquipmentService {
    /**
     * 获取网段IP设备
     * @param subnet
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
     Map<String, String> scanSubnet(String subnet, int start, int end) throws Exception;

    }
