package com.pig4cloud.pig.alerter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.pig4cloud.pig.common.alert.mapper.NoticeReceiverMapper;
import com.pig4cloud.pig.common.alert.mapper.NoticeRuleMapper;
import com.pig4cloud.pig.common.alert.mapper.NoticeTemplateMapper;
import com.pig4cloud.pig.common.alert.notice.AlertNoticeDispatch;
import com.pig4cloud.pig.common.alert.service.NoticeConfigService;
import com.pig4cloud.pig.common.core.cache.CacheFactory;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.entity.alerter.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息通知配置管理服务实现
 *
 * @author pig4cloud
 */
@Service
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class NoticeConfigServiceImpl implements NoticeConfigService, CommandLineRunner {

	private static final Map<Byte, NoticeTemplate> PRESET_TEMPLATE = new HashMap<>(16);

	private final NoticeReceiverMapper noticeReceiverMapper;

	private final NoticeRuleMapper noticeRuleMapper;

	private final NoticeTemplateMapper noticeTemplateMapper;

	private final AlertNoticeDispatch dispatcherAlarm;

	public NoticeConfigServiceImpl(NoticeReceiverMapper noticeReceiverMapper, NoticeRuleMapper noticeRuleMapper,
			NoticeTemplateMapper noticeTemplateMapper, @Lazy AlertNoticeDispatch dispatcherAlarm) {
		this.noticeReceiverMapper = noticeReceiverMapper;
		this.noticeRuleMapper = noticeRuleMapper;
		this.noticeTemplateMapper = noticeTemplateMapper;
		this.dispatcherAlarm = dispatcherAlarm;
	}

	/**
	 * 动态条件分页查询通知接收人
	 */
	@Override
	public IPage<NoticeReceiver> getNoticeReceivers(String name, int pageIndex, int pageSize) {
		LambdaQueryWrapper<NoticeReceiver> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotBlank(name)) {
			wrapper.like(NoticeReceiver::getName, "%" + name.toLowerCase() + "%");
		}
		wrapper.orderByDesc(NoticeReceiver::getId);
		Page<NoticeReceiver> page = new Page<>(pageIndex + 1, pageSize);
		return noticeReceiverMapper.selectPage(page, wrapper);
	}

	/**
	 * 查询所有通知接收人
	 */
	@Override
	public List<NoticeReceiver> getAllNoticeReceivers() {
		return noticeReceiverMapper.selectList(null);
	}

	/**
	 * 动态条件分页查询通知模板（支持预设模板和自定义模板）
	 */
	@Override
	public IPage<NoticeTemplate> getNoticeTemplates(String name, boolean preset, int pageIndex, int pageSize) {
		if (preset) {
			// 查询预设模板
			List<NoticeTemplate> defaultTemplates = new LinkedList<>(PRESET_TEMPLATE.values());

			// 按名称模糊过滤（不区分大小写）
			List<NoticeTemplate> filteredDefaultTemplates = defaultTemplates.stream()
				.filter(template -> StringUtils.isBlank(name)
						|| template.getName().toLowerCase().contains(name.toLowerCase()))
				.collect(Collectors.toList());

			// 分页逻辑
			int totalItems = filteredDefaultTemplates.size();
			int fromIndex = Math.min(pageIndex * pageSize, totalItems);
			int toIndex = Math.min(fromIndex + pageSize, totalItems);

			Page<NoticeTemplate> page = new Page<>(pageIndex + 1, pageSize, totalItems);
			if (fromIndex >= totalItems) {
				page.setRecords(Collections.emptyList());
			} else {
				page.setRecords(filteredDefaultTemplates.subList(fromIndex, toIndex));
			}
			return page;
		} else {
			// 查询自定义模板
			LambdaQueryWrapper<NoticeTemplate> wrapper = new LambdaQueryWrapper<>();
			if (StringUtils.isNotBlank(name)) {
				wrapper.like(NoticeTemplate::getName, "%" + name.toLowerCase() + "%");
			}
			wrapper.orderByDesc(NoticeTemplate::getId);
			Page<NoticeTemplate> page = new Page<>(pageIndex + 1, pageSize);
			return noticeTemplateMapper.selectPage(page, wrapper);
		}
	}

	/**
	 * 查询所有通知模板（包含预设模板和自定义模板）
	 */
	@Override
	public List<NoticeTemplate> getAllNoticeTemplates() {
		List<NoticeTemplate> defaultTemplates = new LinkedList<>(PRESET_TEMPLATE.values());
		defaultTemplates.addAll(noticeTemplateMapper.selectList(null));
		return defaultTemplates;
	}

	/**
	 * 动态条件分页查询通知规则
	 */
	@Override
	public IPage<NoticeRule> getNoticeRules(String name, int pageIndex, int pageSize) {
		LambdaQueryWrapper<NoticeRule> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotBlank(name)) {
			wrapper.like(NoticeRule::getName, "%" + name.toLowerCase() + "%");
		}
		wrapper.orderByDesc(NoticeRule::getId);
		Page<NoticeRule> page = new Page<>(pageIndex + 1, pageSize);
		return noticeRuleMapper.selectPage(page, wrapper);
	}

	/**
	 * 新增通知接收人
	 */
	@Override
	public void addReceiver(NoticeReceiver noticeReceiver) {
		noticeReceiverMapper.insert(noticeReceiver);
	}

	/**
	 * 修改通知接收人
	 */
	@Override
	public void editReceiver(NoticeReceiver noticeReceiver) {
		noticeReceiverMapper.updateById(noticeReceiver);
	}

	/**
	 * 删除通知接收人
	 */
	@Override
	public void deleteReceiver(Long receiverId) {
		noticeReceiverMapper.deleteById(receiverId);
	}

	/**
	 * 新增通知规则
	 */
	@Override
	public void addNoticeRule(NoticeRule noticeRule) {
		noticeRuleMapper.insert(noticeRule);
		clearNoticeRulesCache();
	}

	/**
	 * 修改通知规则
	 */
	@Override
	public void editNoticeRule(NoticeRule noticeRule) {
		noticeRuleMapper.updateById(noticeRule);
		clearNoticeRulesCache();
	}

	/**
	 * 删除通知规则
	 */
	@Override
	public void deleteNoticeRule(Long ruleId) {
		noticeRuleMapper.deleteById(ruleId);
		clearNoticeRulesCache();
	}

	/**
	 * 根据告警信息匹配通知规则，筛选出需要通知的接收人
	 */
	@Override
	public List<NoticeRule> getReceiverFilterRule(GroupAlert alert) {
		// 使用缓存
		List<NoticeRule> rules = CacheFactory.getNoticeCache();
		if (rules == null) {
			rules = noticeRuleMapper
				.selectList(new LambdaQueryWrapper<NoticeRule>().eq(NoticeRule::isEnable, true));
			CacheFactory.setNoticeCache(rules);
		}

		// 根据标签、星期、时间段过滤匹配的通知规则
		return rules.stream().filter(rule -> {
			if (!rule.isFilterAll()) {
				// 过滤标签
				if (rule.getLabels() != null && !rule.getLabels().isEmpty()) {
					boolean labelMatch = rule.getLabels().entrySet().stream().allMatch(labelItem -> {
						if (!alert.getCommonLabels().containsKey(labelItem.getKey())) {
							return false;
						}
						String alertLabelValue = alert.getCommonLabels().get(labelItem.getKey());
						return Objects.equals(labelItem.getValue(), alertLabelValue);
					});
					if (!labelMatch) {
						return false;
					}
				}
			}

			LocalDateTime nowDate = LocalDateTime.now();
			// 过滤星期
			int currentDayOfWeek = nowDate.toLocalDate().getDayOfWeek().getValue();
			if (rule.getDays() != null && !rule.getDays().isEmpty()) {
				boolean dayMatch = rule.getDays().stream().anyMatch(item -> item == currentDayOfWeek);
				if (!dayMatch) {
					return false;
				}
			}
			// 过滤时间段
			LocalTime nowTime = nowDate.toLocalTime();
			boolean startMatch = rule.getPeriodStart() == null
					|| nowTime.isAfter(rule.getPeriodStart().toLocalTime())
					|| (rule.getPeriodEnd() != null && rule.getPeriodStart().isAfter(rule.getPeriodEnd())
							&& nowTime.isBefore(rule.getPeriodStart().toLocalTime()));
			boolean endMatch = rule.getPeriodEnd() == null || nowTime.isBefore(rule.getPeriodEnd().toLocalTime());
			return startMatch && endMatch;
		}).collect(Collectors.toList());
	}

	/**
	 * 根据模板ID查询模板信息
	 */
	@Override
	public NoticeTemplate getOneTemplateById(Long id) {
		return noticeTemplateMapper.selectById(id);
	}

	/**
	 * 根据接收人ID查询接收人信息
	 */
	@Override
	public NoticeReceiver getReceiverById(Long receiverId) {
		return noticeReceiverMapper.selectById(receiverId);
	}

	/**
	 * 根据规则ID查询通知规则
	 */
	@Override
	public NoticeRule getNoticeRulesById(Long ruleId) {
		return noticeRuleMapper.selectById(ruleId);
	}

	/**
	 * 新增通知模板
	 */
	@Override
	public void addNoticeTemplate(NoticeTemplate noticeTemplate) {
		noticeTemplateMapper.insert(noticeTemplate);
		clearNoticeRulesCache();
	}

	/**
	 * 修改通知模板
	 */
	@Override
	public void editNoticeTemplate(NoticeTemplate noticeTemplate) {
		noticeTemplateMapper.updateById(noticeTemplate);
		clearNoticeRulesCache();
	}

	/**
	 * 删除通知模板
	 */
	@Override
	public void deleteNoticeTemplate(Long templateId) {
		noticeTemplateMapper.deleteById(templateId);
		clearNoticeRulesCache();
	}

	/**
	 * 根据模板ID查询通知模板（Optional包装）
	 */
	@Override
	public Optional<NoticeTemplate> getNoticeTemplatesById(Long templateId) {
		return Optional.ofNullable(noticeTemplateMapper.selectById(templateId));
	}

	/**
	 * 根据模板类型查询默认预设通知模板
	 */
	@Override
	public NoticeTemplate getDefaultNoticeTemplateByType(Byte type) {
		if (type == null) {
			return null;
		}
		return PRESET_TEMPLATE.get(type);
	}

	/**
	 * 发送测试通知消息
	 */
	@Override
	public boolean sendTestMsg(NoticeReceiver noticeReceiver) {
		Map<String, String> labels = new HashMap<>(8);
		labels.put(CommonConstants.LABEL_INSTANCE, "1000000");
		labels.put(CommonConstants.LABEL_ALERT_NAME, "CPU Usage Alert");
		labels.put(CommonConstants.LABEL_INSTANCE_HOST, "127.0.0.1");
		Map<String, String> annotations = new HashMap<>(8);
		annotations.put("suggest", "Please check the CPU usage of the server");
		SingleAlert singleAlert1 = SingleAlert.builder()
			.labels(labels)
			.content("test send msg! \\n This is the test data. It is proved that it can be received successfully")
			.startAt(System.currentTimeMillis())
			.activeAt(System.currentTimeMillis())
			.endAt(System.currentTimeMillis())
			.triggerTimes(2)
			.annotations(annotations)
			.status("firing")
			.build();
		SingleAlert singleAlert2 = SingleAlert.builder()
			.labels(labels)
			.content("test send msg! \\n This is the test data. It is proved that it can be received successfully")
			.startAt(System.currentTimeMillis())
			.activeAt(System.currentTimeMillis())
			.endAt(System.currentTimeMillis())
			.triggerTimes(4)
			.annotations(annotations)
			.status("firing")
			.build();
		GroupAlert groupAlert = GroupAlert.builder()
			.commonLabels(Map.of(CommonConstants.LABEL_ALERT_NAME, "CPU Usage Alert"))
			.commonAnnotations(annotations)
			.alerts(List.of(singleAlert1, singleAlert2))
			.status("firing")
			.build();
		return dispatcherAlarm.sendNoticeMsg(noticeReceiver, null, groupAlert);
	}

	/**
	 * 清除通知规则缓存
	 */
	private void clearNoticeRulesCache() {
		CacheFactory.clearNoticeCache();
	}

	/**
	 * 应用启动时加载预设通知模板
	 */
	@Override
	public void run(String... args) throws Exception {
		try {
			log.info("load default notice template in internal jar");
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath:templates/*.*");
			for (Resource resource : resources) {
				if (resource.getFilename() == null
						|| (!resource.getFilename().endsWith("txt") && !resource.getFilename().endsWith("html"))) {
					log.warn("Ignore the template file {}.", resource.getFilename());
					continue;
				}
				try (InputStream inputStream = resource.getInputStream()) {
					byte[] bytes = new byte[inputStream.available()];
					inputStream.read(bytes);
					String content = new String(bytes, StandardCharsets.UTF_8);
					NoticeTemplate template = new NoticeTemplate();
					String name = resource.getFilename().replace(".txt", "").replace(".html", "");
					String[] names = name.split("-");
					if (names.length != 2) {
						log.warn("Ignore the template file {}.", resource.getFilename());
						continue;
					}
					byte type = Byte.parseByte(names[0]);
					name = names[1];
					template.setName(name);
					template.setType(type);
					template.setPreset(true);
					template.setContent(content);
					template.setGmtUpdate(LocalDateTime.now());
					PRESET_TEMPLATE.put(template.getType(), template);
				}
				catch (IOException e) {
					log.error(e.getMessage(), e);
					log.error("Ignore this template file: {}.", resource.getFilename());
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
