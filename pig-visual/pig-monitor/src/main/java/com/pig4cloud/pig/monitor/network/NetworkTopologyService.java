package com.pig4cloud.pig.monitor.network;

import com.pig4cloud.pig.common.core.entity.network.NetworkTopologyInfo;

import java.util.List;

/**
 * 网络拓扑数据服务
 */
public interface NetworkTopologyService {

    //保存数据
    NetworkTopologyInfo save(NetworkTopologyInfo info);
   // 通过ID删除
    void deleteById(Long id);

    //通过配置ID删除
    void deleteAllByConfigId(String configId);

    //查询所有数据
    List<NetworkTopologyInfo> findAll();

    //通过配置ID查询
    List<NetworkTopologyInfo> findByConfigId(String configId);
   //通过MAC查询
    List<NetworkTopologyInfo> findByMac(String mac);

}
