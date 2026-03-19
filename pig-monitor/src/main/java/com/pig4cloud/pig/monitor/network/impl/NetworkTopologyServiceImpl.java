package com.pig4cloud.pig.monitor.network.impl;

import jakarta.annotation.Resource;
import com.pig4cloud.pig.common.core.entity.network.NetworkTopologyInfo;
import com.pig4cloud.pig.monitor.network.NetworkTopologyService;
import com.pig4cloud.pig.monitor.network.dao.NetworkTopologyDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网络拓扑数据服务实现
 */
@Service
public class NetworkTopologyServiceImpl implements NetworkTopologyService {

    @Resource
    private NetworkTopologyDao repository;

    @Override
    public NetworkTopologyInfo save(NetworkTopologyInfo info) {
        return repository.save(info);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAllByConfigId(String configId) {
        repository.deleteAllByConfigId(configId);
    }

    @Override
    public List<NetworkTopologyInfo> findAll() {
        return repository.findAll();
    }

    @Override
    public List<NetworkTopologyInfo> findByConfigId(String configId) {
        return repository.findByConfigId(configId);
    }

    @Override
    public List<NetworkTopologyInfo> findByMac(String mac) {
        return repository.findByMac(mac);
    }

}
