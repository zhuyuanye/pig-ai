package com.pig4cloud.pig.monitor.network.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import com.pig4cloud.pig.monitor.network.SnmpConfigService;
import com.pig4cloud.pig.monitor.mapper.SnmpConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SNMP 配置服务实现
 */
@Service
@RequiredArgsConstructor
public class SnmpConfigServiceImpl implements SnmpConfigService {

    private final SnmpConfigMapper snmpConfigMapper;

    @Override
    public SnmpConfigInfo create(SnmpConfigInfo configInfo) {
        if (configInfo.getId() != null) {
            snmpConfigMapper.updateById(configInfo);
        } else {
            snmpConfigMapper.insert(configInfo);
        }
        return configInfo;
    }

    @Override
    public void deleteById(Long id) {
        snmpConfigMapper.deleteById(id);
    }

    @Override
    public List<SnmpConfigInfo> getAll() {
        return snmpConfigMapper.selectList(null);
    }

    @Override
    public SnmpConfigInfo getById(Long id) {
        return snmpConfigMapper.selectById(id);
    }

    @Override
    public SnmpConfigInfo update(SnmpConfigInfo configInfo) {
        snmpConfigMapper.updateById(configInfo);
        return configInfo;
    }

    @Override
    public List<SnmpConfigInfo> getRunningConfigs() {
        return snmpConfigMapper.selectList(
                new LambdaQueryWrapper<SnmpConfigInfo>().eq(SnmpConfigInfo::getIsRunType, "1"));
    }

}
