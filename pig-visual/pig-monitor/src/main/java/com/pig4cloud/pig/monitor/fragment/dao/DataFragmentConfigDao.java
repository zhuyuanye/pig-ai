package com.pig4cloud.pig.monitor.fragment.dao;

import com.pig4cloud.pig.common.core.entity.fragment.DataFragmentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据碎片配置
 */
public interface DataFragmentConfigDao extends JpaRepository<DataFragmentConfig, Long>, JpaSpecificationExecutor<DataFragmentConfig> {

}
