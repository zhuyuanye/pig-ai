package com.pig4cloud.pig.common.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.alerter.AlertSilence;
import org.apache.ibatis.annotations.Mapper;

/**
 * 告警静默规则 Mapper
 *
 * @author pig4cloud
 */
@Mapper
public interface AlertSilenceMapper extends BaseMapper<AlertSilence> {

}
