package com.pig4cloud.pig.monitor.network.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import com.pig4cloud.pig.common.core.entity.network.NetworkTopologyInfo;
import com.pig4cloud.pig.monitor.network.NetworkTopologyService;
import com.pig4cloud.pig.monitor.mapper.NetworkTopologyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网络拓扑数据服务实现
 */
@Service
@RequiredArgsConstructor
public class NetworkTopologyServiceImpl implements NetworkTopologyService {

    private final NetworkTopologyMapper networkTopologyMapper;

    @Override
    public NetworkTopologyInfo save(NetworkTopologyInfo info) {
        if (info.getId() != null) {
            networkTopologyMapper.updateById(info);
        } else {
            networkTopologyMapper.insert(info);
        }
        return info;
    }

    @Override
    public void deleteById(Long id) {
        networkTopologyMapper.deleteById(id);
    }

    @Override
    public void deleteAllByConfigId(String configId) {
        networkTopologyMapper.delete(
                new LambdaQueryWrapper<NetworkTopologyInfo>().eq(NetworkTopologyInfo::getConfigId, configId));
    }

    @Override
    public List<NetworkTopologyInfo> findAll() {
        return networkTopologyMapper.selectList(null);
    }

    @Override
    public List<NetworkTopologyInfo> findByConfigId(String configId) {
        return networkTopologyMapper.selectList(
                new LambdaQueryWrapper<NetworkTopologyInfo>().eq(NetworkTopologyInfo::getConfigId, configId));
    }

    @Override
    public List<NetworkTopologyInfo> findByMac(String mac) {
        return networkTopologyMapper.selectList(
                new LambdaQueryWrapper<NetworkTopologyInfo>().eq(NetworkTopologyInfo::getMac, mac));
    }

}
