package com.pig4cloud.pig.alerter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.alert.mapper.AlertSilenceMapper;
import com.pig4cloud.pig.common.alert.service.AlertSilenceService;
import com.pig4cloud.pig.common.core.cache.CacheFactory;
import com.pig4cloud.pig.common.core.entity.alerter.AlertSilence;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 告警静默管理服务实现
 *
 * @author pig4cloud
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class AlertSilenceServiceImpl implements AlertSilenceService {

	private final AlertSilenceMapper alertSilenceMapper;

	@Override
	public void validate(AlertSilence alertSilence, boolean isModify) throws IllegalArgumentException {
		// todo
		// 如果周期性静默未设置生效星期，则默认全选
		if (alertSilence.getType() == 1 && alertSilence.getDays() == null) {
			alertSilence
				.setDays(Arrays.asList((byte) 7, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6));
		}
	}

	/**
	 * 新增告警静默策略
	 */
	@Override
	public void addAlertSilence(AlertSilence alertSilence) throws RuntimeException {
		alertSilenceMapper.insert(alertSilence);
		clearAlertSilencesCache();
	}

	/**
	 * 修改告警静默策略
	 */
	@Override
	public void modifyAlertSilence(AlertSilence alertSilence) throws RuntimeException {
		alertSilenceMapper.updateById(alertSilence);
		clearAlertSilencesCache();
	}

	/**
	 * 根据ID查询告警静默策略
	 */
	@Override
	public AlertSilence getAlertSilence(long silenceId) throws RuntimeException {
		return alertSilenceMapper.selectById(silenceId);
	}

	/**
	 * 批量删除告警静默策略
	 */
	@Override
	public void deleteAlertSilences(Set<Long> silenceIds) throws RuntimeException {
		alertSilenceMapper.deleteByIds(silenceIds);
		clearAlertSilencesCache();
	}

	/**
	 * 动态条件分页查询告警静默策略
	 */
	@Override
	public IPage<AlertSilence> getAlertSilences(List<Long> silenceIds, String search, String sort, String order,
			int pageIndex, int pageSize) {
		LambdaQueryWrapper<AlertSilence> wrapper = new LambdaQueryWrapper<>();
		if (silenceIds != null && !silenceIds.isEmpty()) {
			wrapper.in(AlertSilence::getId, silenceIds);
		}
		if (StringUtils.hasText(search)) {
			wrapper.like(AlertSilence::getName, "%" + search.toLowerCase() + "%");
		}
		// 排序
		boolean isAsc = "asc".equalsIgnoreCase(order);
		wrapper.orderBy(true, isAsc,
				"gmt_create".equals(sort) ? AlertSilence::getGmtCreate : AlertSilence::getId);

		Page<AlertSilence> page = new Page<>(pageIndex + 1, pageSize);
		return alertSilenceMapper.selectPage(page, wrapper);
	}

	/**
	 * 清除告警静默缓存
	 */
	private void clearAlertSilencesCache() {
		CacheFactory.clearAlertSilenceCache();
	}

}
