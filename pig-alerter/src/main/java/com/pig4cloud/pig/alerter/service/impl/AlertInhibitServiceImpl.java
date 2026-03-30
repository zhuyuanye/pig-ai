package com.pig4cloud.pig.alerter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.alert.mapper.AlertInhibitMapper;
import com.pig4cloud.pig.common.alert.reduce.AlarmInhibitReduce;
import com.pig4cloud.pig.common.alert.service.AlertInhibitService;
import com.pig4cloud.pig.common.core.entity.alerter.AlertInhibit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 告警抑制管理服务实现
 *
 * @author pig4cloud
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class AlertInhibitServiceImpl implements AlertInhibitService {

	private final AlertInhibitMapper alertInhibitMapper;

	private final AlarmInhibitReduce alarmInhibitReduce;

	@Override
	public void validate(AlertInhibit alertInhibit, boolean isModify) throws IllegalArgumentException {
		// todo
	}

	/**
	 * 新增告警抑制规则
	 */
	@Override
	public void addAlertInhibit(AlertInhibit alertInhibit) throws RuntimeException {
		alertInhibitMapper.insert(alertInhibit);
		refreshAlertInhibitsCache();
	}

	/**
	 * 修改告警抑制规则
	 */
	@Override
	public void modifyAlertInhibit(AlertInhibit alertInhibit) throws RuntimeException {
		alertInhibitMapper.updateById(alertInhibit);
		refreshAlertInhibitsCache();
	}

	/**
	 * 根据ID查询告警抑制规则
	 */
	@Override
	public AlertInhibit getAlertInhibit(long inhibitId) throws RuntimeException {
		return alertInhibitMapper.selectById(inhibitId);
	}

	/**
	 * 批量删除告警抑制规则
	 */
	@Override
	public void deleteAlertInhibits(Set<Long> inhibitIds) throws RuntimeException {
		alertInhibitMapper.deleteByIds(inhibitIds);
		refreshAlertInhibitsCache();
	}

	/**
	 * 动态条件分页查询告警抑制规则
	 */
	@Override
	public IPage<AlertInhibit> getAlertInhibits(List<Long> inhibitIds, String search, String sort, String order,
			int pageIndex, int pageSize) {
		LambdaQueryWrapper<AlertInhibit> wrapper = new LambdaQueryWrapper<>();
		if (inhibitIds != null && !inhibitIds.isEmpty()) {
			wrapper.in(AlertInhibit::getId, inhibitIds);
		}
		if (StringUtils.hasText(search)) {
			wrapper.like(AlertInhibit::getName, "%" + search.toLowerCase() + "%");
		}
		// 排序
		boolean isAsc = "asc".equalsIgnoreCase(order);
		wrapper.orderBy(true, isAsc,
				"gmt_create".equals(sort) ? AlertInhibit::getGmtCreate : AlertInhibit::getId);

		Page<AlertInhibit> page = new Page<>(pageIndex + 1, pageSize);
		return alertInhibitMapper.selectPage(page, wrapper);
	}

	/**
	 * 刷新告警抑制规则缓存
	 */
	private void refreshAlertInhibitsCache() {
		alarmInhibitReduce.refreshInhibitRules(alertInhibitMapper
				.selectList(new LambdaQueryWrapper<AlertInhibit>().eq(AlertInhibit::getEnable, true)));
	}

}
