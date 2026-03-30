package com.pig4cloud.pig.common.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.alerter.AlertInhibit;
import org.apache.ibatis.annotations.Mapper;

/**
 * 告警抑制规则 Mapper
 *
 * @author pig4cloud
 */
@Mapper
public interface AlertInhibitMapper extends BaseMapper<AlertInhibit> {

}
