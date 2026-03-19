package com.pig4cloud.pig.monitor.network.impl;

import jakarta.annotation.Resource;
import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import com.pig4cloud.pig.monitor.network.SnmpConfigService;
import com.pig4cloud.pig.monitor.network.dao.SnmpConfigDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnmpConfigServiceImpl implements SnmpConfigService {

    @Resource
    private SnmpConfigDao repository;

    @Override
    public SnmpConfigInfo create(SnmpConfigInfo configInfo) {
        return repository.save(configInfo);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<SnmpConfigInfo> getAll() {
        return repository.findAll();
    }

    @Override
    public SnmpConfigInfo getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public SnmpConfigInfo update(SnmpConfigInfo configInfo) {
        return repository.save(configInfo);
    }

    @Override
    public List<SnmpConfigInfo> getRunningConfigs() {
        return repository.findByIsRunType("1");
    }
}
