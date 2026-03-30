package com.pig4cloud.pig.alerter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.alert.mapper.GroupAlertMapper;
import com.pig4cloud.pig.common.alert.mapper.SingleAlertMapper;
import com.pig4cloud.pig.common.alert.dto.AlertSummary;
import com.pig4cloud.pig.common.alert.service.AlertService;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.entity.alerter.GroupAlert;
import com.pig4cloud.pig.common.core.entity.alerter.SingleAlert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 告警信息管理服务实现
 *
 * @author pig4cloud
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

	private final GroupAlertMapper groupAlertMapper;

	private final SingleAlertMapper singleAlertMapper;

	@Override
	public IPage<SingleAlert> getSingleAlerts(String status, String search, String sort, String order, int pageIndex,
			int pageSize) {
		LambdaQueryWrapper<SingleAlert> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.hasText(status)) {
			wrapper.eq(SingleAlert::getStatus, status);
		}
		if (StringUtils.hasText(search)) {
			wrapper.and(w -> w.like(SingleAlert::getContent, search)
				.or()
				.like(SingleAlert::getFingerprint, search));
		}
		boolean isAsc = "asc".equalsIgnoreCase(order);
		wrapper.orderBy(true, isAsc, "gmt_create".equals(sort) ? SingleAlert::getGmtCreate : SingleAlert::getId);

		Page<SingleAlert> page = new Page<>(pageIndex + 1, pageSize);
		return singleAlertMapper.selectPage(page, wrapper);
	}

	@Override
	public IPage<GroupAlert> getGroupAlerts(String status, String search, String sort, String order, int pageIndex,
			int pageSize, String startTime, String endTime) {
		LambdaQueryWrapper<GroupAlert> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.hasText(status)) {
			wrapper.eq(GroupAlert::getStatus, status);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.hasText(startTime)) {
			wrapper.ge(GroupAlert::getGmtCreate, LocalDateTime.parse(startTime, formatter));
		}
		if (StringUtils.hasText(endTime)) {
			wrapper.lt(GroupAlert::getGmtCreate, LocalDateTime.parse(endTime, formatter));
		}
		if (StringUtils.hasText(search)) {
			wrapper.and(w -> w.like(GroupAlert::getGroupKey, search));
		}
		boolean isAsc = "asc".equalsIgnoreCase(order);
		wrapper.orderBy(true, isAsc, "gmt_create".equals(sort) ? GroupAlert::getGmtCreate : GroupAlert::getId);

		Page<GroupAlert> page = new Page<>(pageIndex + 1, pageSize);
		IPage<GroupAlert> groupAlertPage = groupAlertMapper.selectPage(page, wrapper);

		// 填充关联的单条告警
		for (GroupAlert groupAlert : groupAlertPage.getRecords()) {
			List<String> firingAlerts = groupAlert.getAlertFingerprints();
			if (firingAlerts != null && !firingAlerts.isEmpty()) {
				List<SingleAlert> singleAlerts = singleAlertMapper
					.selectList(new LambdaQueryWrapper<SingleAlert>().in(SingleAlert::getFingerprint, firingAlerts));
				groupAlert.setAlerts(singleAlerts);
			}
			else {
				groupAlert.setAlerts(new ArrayList<>());
			}
		}
		return groupAlertPage;
	}

	@Override
	public void deleteGroupAlerts(HashSet<Long> ids) {
		List<GroupAlert> groupAlerts = groupAlertMapper
			.selectList(new LambdaQueryWrapper<GroupAlert>().in(GroupAlert::getId, ids));
		for (GroupAlert groupAlert : groupAlerts) {
			List<String> firingAlerts = groupAlert.getAlertFingerprints();
			if (firingAlerts != null && !firingAlerts.isEmpty()) {
				singleAlertMapper
					.delete(new LambdaQueryWrapper<SingleAlert>().in(SingleAlert::getFingerprint, firingAlerts));
			}
		}
		groupAlertMapper.deleteByIds(ids);
	}

	@Override
	public void deleteSingleAlerts(HashSet<Long> ids) {
		singleAlertMapper.deleteByIds(ids);
	}

	@Override
	public void editGroupAlertStatus(String status, List<Long> ids) {
		groupAlertMapper.updateStatusByIds(status, ids);
	}

	@Override
	public void editSingleAlertStatus(String status, List<Long> ids) {
		singleAlertMapper.updateStatusByIds(status, ids);
	}

	@Override
	public AlertSummary getAlertsSummary() {
		AlertSummary alertSummary = new AlertSummary();
		// 统计触发中的告警
		List<SingleAlert> firingAlerts = singleAlertMapper.selectList(
				new LambdaQueryWrapper<SingleAlert>().eq(SingleAlert::getStatus, CommonConstants.ALERT_STATUS_FIRING));

		int emergencyNum = 0;
		int criticalNum = 0;
		int warningNum = 0;
		for (SingleAlert alert : firingAlerts) {
			String severity = alert.getLabels() != null
					? alert.getLabels().get(CommonConstants.LABEL_ALERT_SEVERITY) : null;
			if (severity != null) {
				switch (severity) {
					case CommonConstants.ALERT_SEVERITY_EMERGENCY -> emergencyNum++;
					case CommonConstants.ALERT_SEVERITY_CRITICAL -> criticalNum++;
					case CommonConstants.ALERT_SEVERITY_WARNING -> warningNum++;
					default -> {
					}
				}
			}
		}
		alertSummary.setPriorityCriticalNum(criticalNum);
		alertSummary.setPriorityEmergencyNum(emergencyNum);
		alertSummary.setPriorityWarningNum(warningNum);

		long total = singleAlertMapper.selectCount(null);
		alertSummary.setTotal(total);
		long resolved = total - firingAlerts.size();
		alertSummary.setDealNum(resolved);
		try {
			if (total == 0) {
				alertSummary.setRate(100);
			}
			else {
				float rate = BigDecimal.valueOf(100 * (float) resolved / total)
					.setScale(2, RoundingMode.HALF_UP)
					.floatValue();
				alertSummary.setRate(rate);
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return alertSummary;
	}

}
