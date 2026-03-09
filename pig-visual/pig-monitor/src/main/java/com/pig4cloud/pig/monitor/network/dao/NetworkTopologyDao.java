package com.pig4cloud.pig.monitor.network.dao;

import com.pig4cloud.pig.common.core.entity.network.NetworkTopologyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 网络拓扑数据DAO
 */
public interface NetworkTopologyDao extends JpaRepository<NetworkTopologyInfo, Long>, JpaSpecificationExecutor<NetworkTopologyInfo> {

    //通过配置ID查询
    List<NetworkTopologyInfo> findByConfigId(String configId);

   //通过MAC查询
    List<NetworkTopologyInfo> findByMac(String mac);

    //通过配置ID删除
    void deleteAllByConfigId (String configId);
}
