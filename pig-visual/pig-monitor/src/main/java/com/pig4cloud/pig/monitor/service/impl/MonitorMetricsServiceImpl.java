package com.pig4cloud.pig.monitor.service.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import com.pig4cloud.pig.common.core.entity.dto.MetricsHistoryData;
import com.pig4cloud.pig.common.core.entity.dto.Value;
import com.pig4cloud.pig.common.core.entity.dto.ValuePlainDTO;
import com.pig4cloud.pig.common.core.entity.job.Job;
import com.pig4cloud.pig.common.core.entity.job.Metrics;
import com.pig4cloud.pig.common.core.entity.manager.Monitor;
import com.pig4cloud.pig.monitor.service.AppService;
import com.pig4cloud.pig.monitor.service.MonitorMetricsService;
import com.pig4cloud.pig.common.warehouse.service.MetricsDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName MonitorMetricsServiceImpl
 * @Author Administrator
 * @Date 2025/06/17 14:34
 * @Description 监控指标
 */
@Slf4j
@Service
public class MonitorMetricsServiceImpl implements MonitorMetricsService {

    private final MetricsDataService metricsDataService;
    private final AppService appService;
    private final ExecutorService executorService;

    public MonitorMetricsServiceImpl(MetricsDataService metricsDataService, AppService appService) {
        this.metricsDataService = metricsDataService;
        this.appService = appService;

        Runtime runtime = Runtime.getRuntime();
        int corePoolSize = Math.max(8, runtime.availableProcessors());
        int maximumPoolSize = Math.max(16, runtime.availableProcessors());
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setUncaughtExceptionHandler((thread, throwable) -> {
                log.error("monitorMetrics has uncaughtException.");
                log.error(throwable.getMessage(), throwable);
            })
            .setDaemon(true)
            .setNameFormat("monitorMetrics-%d")
            .build();
        this.executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(5000), threadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    @Override
    public List<ValuePlainDTO> metricsValueList(Monitor monitor, String metrics, String metric, String label, String history, Boolean interval) {

        String app = monitor.getApp();
        Job job = appService.getAppDefine(app);
        List<Metrics> JobMetrics = job.getMetrics();
        Metrics monitorMetrics = JobMetrics.stream().filter(item -> item.getName().equals(metrics)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("metrics does not exists: " + metrics));
        List<Metrics.Field> fields = monitorMetrics.getFields();

        List<ValuePlainDTO> valuePlainDTOS = new ArrayList<>();
        CompletionService<List<ValuePlainDTO>> completionService = new ExecutorCompletionService<>(executorService);
        if (null != metric) {
            Metrics.Field field = fields.stream().filter(item -> item.getField().equals(metric)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("metric does not exists: " + metric));

            MetricsHistoryData historyData = metricsDataService.getMetricHistoryData(monitor.getId(), app, monitorMetrics.getName(), field.getField(), label, history, interval);
            return getValuePlainDTOList(historyData.getValues(), field.getField(), field.getI18n().get("zh-CN"));
        } else {
            for (Metrics.Field field : fields) {
                completionService.submit(() -> {
                    String fieldName = field.getI18n().get("zh-CN");
                    MetricsHistoryData historyData = metricsDataService.getMetricHistoryData(monitor.getId(), app, monitorMetrics.getName(), field.getField(), label, history, interval);
                    Map<String, List<Value>> values = historyData.getValues();

                    return getValuePlainDTOList(values, field.getField(), fieldName);
                });
            }
            try {
                for (Metrics.Field field : fields) {
                    log.info("field:{} take ", field.getField());
                    Future<List<ValuePlainDTO>> completedFuture = completionService.take();
                    List<ValuePlainDTO> result = completedFuture.get();
                    valuePlainDTOS.addAll(result);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return valuePlainDTOS;
    }

    private List<ValuePlainDTO> getValuePlainDTOList(Map<String, List<Value>> values, String field, String fieldName) {
        List<ValuePlainDTO> subValuePlainDTOS = new ArrayList<>();
        for (Map.Entry<String, List<Value>> entry : values.entrySet()) {
            List<Value> valueList = entry.getValue();
            for (Value value : valueList) {
                ValuePlainDTO valuePlainDTO = new ValuePlainDTO();
                valuePlainDTO.setField(field);
                valuePlainDTO.setFieldName(fieldName);
                valuePlainDTO.setOrigin(value.getOrigin());
                valuePlainDTO.setMean(value.getMean());
                valuePlainDTO.setMedian(value.getMedian());
                valuePlainDTO.setMin(value.getMin());
                valuePlainDTO.setMax(value.getMax());
                valuePlainDTO.setTime(value.getTime());
                valuePlainDTO.setTimeFormat(DateFormatUtils.format(value.getTime(), "yyyy-MM-dd HH:mm:ss"));
                subValuePlainDTOS.add(valuePlainDTO);
            }
        }
        return subValuePlainDTOS;
    }
}
