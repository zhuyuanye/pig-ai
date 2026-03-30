package com.pig4cloud.pig.alerter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.alert.mapper.AlertGroupConvergeMapper;
import com.pig4cloud.pig.common.alert.reduce.AlarmGroupReduce;
import com.pig4cloud.pig.common.alert.service.AlertGroupConvergeService;
import com.pig4cloud.pig.common.core.entity.alerter.AlertGroupConverge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 告警分组收敛管理服务实现
 *
 * @author pig4cloud
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class AlertGroupConvergeServiceImpl implements AlertGroupConvergeService {

	private final AlertGroupConvergeMapper alertGroupConvergeMapper;

	private final AlarmGroupReduce alarmGroupReduce;

	@Override
	public void validate(AlertGroupConverge alertGroupConverge, boolean isModify) throws IllegalArgumentException {
		// todo
	}

	/**
	 * 新增告警分组收敛策略
	 */
	@Override
	public void addAlertGroupConverge(AlertGroupConverge alertGroupConverge) throws RuntimeException {
		alertGroupConvergeMapper.insert(alertGroupConverge);
		refreshAlertGroupConvergesCache();
	}

	/**
	 * 修改告警分组收敛策略
	 */
	@Override
	public void modifyAlertGroupConverge(AlertGroupConverge alertGroupConverge) throws RuntimeException {
		alertGroupConvergeMapper.updateById(alertGroupConverge);
		refreshAlertGroupConvergesCache();
	}

	/**
	 * 根据ID查询告警分组收敛策略
	 */
	@Override
	public AlertGroupConverge getAlertGroupConverge(long convergeId) throws RuntimeException {
		return alertGroupConvergeMapper.selectById(convergeId);
	}

	/**
	 * 批量删除告警分组收敛策略
	 */
	@Override
	public void deleteAlertGroupConverges(Set<Long> convergeIds) throws RuntimeException {
		alertGroupConvergeMapper.deleteByIds(convergeIds);
		refreshAlertGroupConvergesCache();
	}

	/**
	 * 动态条件分页查询告警分组收敛策略
	 */
	@Override
	public IPage<AlertGroupConverge> getAlertGroupConverges(List<Long> convergeIds, String search, String sort,
			String order, int pageIndex, int pageSize) {
		LambdaQueryWrapper<AlertGroupConverge> wrapper = new LambdaQueryWrapper<>();
		if (convergeIds != null && !convergeIds.isEmpty()) {
			wrapper.in(AlertGroupConverge::getId, convergeIds);
		}
		if (StringUtils.hasText(search)) {
			wrapper.like(AlertGroupConverge::getName, "%" + search.toLowerCase() + "%");
		}
		// 排序
		boolean isAsc = "asc".equalsIgnoreCase(order);
		wrapper.orderBy(true, isAsc,
				"gmt_create".equals(sort) ? AlertGroupConverge::getGmtCreate : AlertGroupConverge::getId);

		Page<AlertGroupConverge> page = new Page<>(pageIndex + 1, pageSize);
		return alertGroupConvergeMapper.selectPage(page, wrapper);
	}

	/**
	 * 刷新告警分组收敛缓存
	 */
	private void refreshAlertGroupConvergesCache() {
		List<AlertGroupConverge> alertGroupConverges = alertGroupConvergeMapper
			.selectList(new LambdaQueryWrapper<AlertGroupConverge>().eq(AlertGroupConverge::getEnable, true));
		alarmGroupReduce.refreshGroupDefines(alertGroupConverges);
	}

}
