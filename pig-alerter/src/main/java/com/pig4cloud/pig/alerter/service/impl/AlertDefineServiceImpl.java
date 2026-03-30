package com.pig4cloud.pig.alerter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.alert.mapper.AlertDefineMapper;
import com.pig4cloud.pig.common.alert.calculate.PeriodicAlertRuleScheduler;
import com.pig4cloud.pig.common.alert.service.AlertDefineImExportService;
import com.pig4cloud.pig.common.alert.service.AlertDefineService;
import com.pig4cloud.pig.common.core.cache.CacheFactory;
import com.pig4cloud.pig.common.core.constants.ExportFileConstants;
import com.pig4cloud.pig.common.core.constants.SignConstants;
import com.pig4cloud.pig.common.core.entity.alerter.AlertDefine;
import com.pig4cloud.pig.common.core.util.FileUtil;
import com.pig4cloud.pig.common.core.util.JexlExpressionRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.pig4cloud.pig.common.core.constants.CommonConstants.ALERT_THRESHOLD_TYPE_REALTIME;

/**
 * 告警规则定义管理服务实现
 *
 * @author pig4cloud
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class AlertDefineServiceImpl implements AlertDefineService {

	private final AlertDefineMapper alertDefineMapper;

	private final PeriodicAlertRuleScheduler periodicAlertRuleScheduler;

	private final Map<String, AlertDefineImExportService> alertDefineImExportServiceMap = new HashMap<>();

	private static final String CONTENT_TYPE = MediaType.APPLICATION_OCTET_STREAM_VALUE + SignConstants.SINGLE_MARK
			+ "charset=" + StandardCharsets.UTF_8;

	public AlertDefineServiceImpl(AlertDefineMapper alertDefineMapper,
			PeriodicAlertRuleScheduler periodicAlertRuleScheduler,
			List<AlertDefineImExportService> alertDefineImExportServiceList) {
		this.alertDefineMapper = alertDefineMapper;
		this.periodicAlertRuleScheduler = periodicAlertRuleScheduler;
		alertDefineImExportServiceList.forEach(it -> alertDefineImExportServiceMap.put(it.type(), it));
	}

	/**
	 * 校验告警规则定义（表达式合法性、名称唯一性）
	 */
	@Override
	public void validate(AlertDefine alertDefine, boolean isModify) throws IllegalArgumentException {
		if (StringUtils.hasText(alertDefine.getExpr())) {
			if (ALERT_THRESHOLD_TYPE_REALTIME.equals(alertDefine.getType())) {
				try {
					JexlExpressionRunner.compile(alertDefine.getExpr());
				}
				catch (Exception e) {
					throw new IllegalArgumentException("alert expr error: " + e.getMessage());
				}
			}
		}
		// 告警规则名称唯一性校验
		LambdaQueryWrapper<AlertDefine> wrapper = new LambdaQueryWrapper<AlertDefine>()
			.eq(AlertDefine::getName, alertDefine.getName());
		AlertDefine existing = alertDefineMapper.selectOne(wrapper);
		if (existing != null) {
			if (!isModify || !existing.getId().equals(alertDefine.getId())) {
				throw new IllegalArgumentException("alert name already exists");
			}
		}
	}

	/**
	 * 新增告警规则定义
	 */
	@Override
	public void addAlertDefine(AlertDefine alertDefine) throws RuntimeException {
		alertDefineMapper.insert(alertDefine);
		periodicAlertRuleScheduler.updateSchedule(alertDefine);
		CacheFactory.clearAlertDefineCache();
	}

	/**
	 * 修改告警规则定义
	 */
	@Override
	public void modifyAlertDefine(AlertDefine alertDefine) throws RuntimeException {
		alertDefineMapper.updateById(alertDefine);
		periodicAlertRuleScheduler.updateSchedule(alertDefine);
		CacheFactory.clearAlertDefineCache();
	}

	/**
	 * 根据ID删除告警规则定义
	 */
	@Override
	public void deleteAlertDefine(long alertId) throws RuntimeException {
		alertDefineMapper.deleteById(alertId);
		periodicAlertRuleScheduler.cancelSchedule(alertId);
		CacheFactory.clearAlertDefineCache();
	}

	/**
	 * 根据ID查询告警规则定义
	 */
	@Override
	public AlertDefine getAlertDefine(long alertId) throws RuntimeException {
		return alertDefineMapper.selectById(alertId);
	}

	/**
	 * 批量删除告警规则定义
	 */
	@Override
	public void deleteAlertDefines(Set<Long> alertIds) throws RuntimeException {
		alertDefineMapper.deleteByIds(alertIds);
		for (Long alertId : alertIds) {
			periodicAlertRuleScheduler.cancelSchedule(alertId);
		}
		CacheFactory.clearAlertDefineCache();
	}

	/**
	 * 动态条件分页查询告警规则定义
	 */
	@Override
	public IPage<AlertDefine> getAlertDefines(List<Long> defineIds, String search, String sort, String order,
			int pageIndex, int pageSize) {
		// 解析搜索关键字列表
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> searchList = Collections.emptyList();
		if (StringUtils.hasText(search)) {
			try {
				searchList = objectMapper.readValue(URLDecoder.decode(search, StandardCharsets.UTF_8),
						new TypeReference<>() {
						});
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Failed to parse search parameter", e);
			}
		}

		// 构建查询条件
		LambdaQueryWrapper<AlertDefine> wrapper = new LambdaQueryWrapper<>();
		if (defineIds != null && !defineIds.isEmpty()) {
			wrapper.in(AlertDefine::getId, defineIds);
		}
		if (!searchList.isEmpty()) {
			List<String> finalSearchList = searchList;
			wrapper.and(w -> {
				for (String keyword : finalSearchList) {
					String lk = "%" + keyword.toLowerCase() + "%";
					w.or(q -> q.like(AlertDefine::getName, lk)
						.or()
						.like(AlertDefine::getExpr, lk)
						.or()
						.like(AlertDefine::getTemplate, lk));
				}
			});
		}

		// 排序
		boolean isAsc = "asc".equalsIgnoreCase(order);
		wrapper.orderBy(true, isAsc, "gmt_create".equals(sort) ? AlertDefine::getGmtCreate : AlertDefine::getId);

		Page<AlertDefine> page = new Page<>(pageIndex + 1, pageSize);
		return alertDefineMapper.selectPage(page, wrapper);
	}

	/**
	 * 导出告警规则定义配置
	 */
	@Override
	public void export(List<Long> ids, String type, HttpServletResponse res) throws Exception {
		var imExportService = alertDefineImExportServiceMap.get(type);
		if (imExportService == null) {
			throw new IllegalArgumentException("not support export type: " + type);
		}
		var fileName = imExportService.getFileName();
		res.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
		res.setContentType(CONTENT_TYPE);
		res.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				"attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
		res.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		imExportService.exportConfig(res.getOutputStream(), ids);
	}

	/**
	 * 导入告警规则定义配置
	 */
	@Override
	public void importConfig(MultipartFile file) throws Exception {
		var type = FileUtil.getFileType(file);
		var fileName = FileUtil.getFileName(file);
		if (!alertDefineImExportServiceMap.containsKey(type)) {
			throw new RuntimeException(ExportFileConstants.FILE + " " + fileName + " is not supported.");
		}
		var imExportService = alertDefineImExportServiceMap.get(type);
		imExportService.importConfig(file.getInputStream());
	}

	/**
	 * 获取所有启用的实时告警规则定义（带缓存）
	 */
	@Override
	public List<AlertDefine> getRealTimeAlertDefines() {
		List<AlertDefine> alertDefines = CacheFactory.getAlertDefineCache();
		if (alertDefines == null) {
			alertDefines = alertDefineMapper.selectByTypeAndEnableTrue(ALERT_THRESHOLD_TYPE_REALTIME);
			CacheFactory.setAlertDefineCache(alertDefines);
		}
		return alertDefines;
	}

}
