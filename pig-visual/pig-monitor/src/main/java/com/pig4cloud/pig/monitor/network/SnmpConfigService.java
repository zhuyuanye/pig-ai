package com.pig4cloud.pig.monitor.network;

import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;

import java.util.List;

public interface SnmpConfigService {
    /**
     * 创建 SNMP 配置信息
     */
    SnmpConfigInfo create(SnmpConfigInfo configInfo);

    /**
     * 根据 ID 删除 SNMP 配置
     */
    void deleteById(Long id);

    /**
     * 获取所有 SNMP 配置
     */
    List<SnmpConfigInfo> getAll();

    /**
     * 根据 ID 获取 SNMP 配置
     */
    SnmpConfigInfo getById(Long id);

    /**
     * 更新 SNMP 配置
     */
    SnmpConfigInfo update(SnmpConfigInfo configInfo);

    /**
     * 获取正在运行的配置
     */
    List<SnmpConfigInfo> getRunningConfigs();
}
