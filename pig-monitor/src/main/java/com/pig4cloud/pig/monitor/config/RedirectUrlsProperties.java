package com.pig4cloud.pig.monitor.config;

import lombok.Data;
import com.pig4cloud.pig.common.core.constants.ConfigConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * url跳转配置 报警 监控 工单
 */
@Data
@Configuration
@ConfigurationProperties(prefix =
        ConfigConstants.FunctionModuleConstants.REDIRECT_URLS)
public class RedirectUrlsProperties {

    private Map<String, String> urls;

}
