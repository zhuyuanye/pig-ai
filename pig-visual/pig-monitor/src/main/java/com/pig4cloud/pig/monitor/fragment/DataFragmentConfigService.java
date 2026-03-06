package com.pig4cloud.pig.monitor.fragment;



import com.pig4cloud.pig.common.core.entity.fragment.DataFragmentConfig;
import com.pig4cloud.pig.monitor.pojo.dto.DataFragmentVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * 数据碎片配置服务
 */
public interface DataFragmentConfigService {
    /**
     * 新增或更新配置
     */
    DataFragmentConfig save(DataFragmentConfig config);

    /**
     * 根据 ID 删除配置
     */
    void deleteById(Long id);

    /**
     * 根据 ID 查询配置
     */
    Optional<DataFragmentConfig> findById(Long id);

    /**
     * 查询全部配置
     */
    List<DataFragmentConfig> findAll();

    /**
     * 根据配置ID 获取数据碎片详情
     * @param id
     * @return
     */
    List<DataFragmentVO> getDataFragmentInfo(Long id) throws ClassNotFoundException, SQLException;


}
