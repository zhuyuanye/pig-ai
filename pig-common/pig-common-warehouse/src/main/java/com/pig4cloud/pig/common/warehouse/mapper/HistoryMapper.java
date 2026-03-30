package com.pig4cloud.pig.common.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.warehouse.History;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HistoryMapper extends BaseMapper<History> {
    int deleteOlderHistoriesRecord(@Param("delNum") int delNum);
    void truncateTable();
}
