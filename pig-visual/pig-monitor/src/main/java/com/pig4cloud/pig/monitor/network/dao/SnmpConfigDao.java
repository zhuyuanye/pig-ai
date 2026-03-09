package com.pig4cloud.pig.monitor.network.dao;

import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 网络拓扑配置类
 */
public interface SnmpConfigDao extends JpaRepository<SnmpConfigInfo, Long>, JpaSpecificationExecutor<SnmpConfigInfo> {
    /**
     * 查询运行中的配置
     * @param isRunType
     * @return
     */
    List<SnmpConfigInfo> findByIsRunType(String isRunType);
}
