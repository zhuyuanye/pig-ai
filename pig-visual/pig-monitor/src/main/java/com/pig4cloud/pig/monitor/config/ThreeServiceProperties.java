package com.pig4cloud.pig.monitor.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.pig4cloud.pig.common.core.constants.ConfigConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Configuration
@ConfigurationProperties(prefix =
    ConfigConstants.FunctionModuleConstants.THREE_SERVICE)
public class ThreeServiceProperties {

    private List<ServiceProperties> services;

    public Map<String, ServiceProperties> serviceMap;

    @Getter
    @Setter
    public static class ServiceProperties {

        /**
         * 登录接口
         */
        private String loginUrl;

        /**
         * 获取key接口
         */
        private String keyUrl;

        /**
         * 跳转地址
         */
        private String targetUrl;

        /**
         * 标识
         */
        private String id;

        /**
         * 用户名
         */
        private String name;

        /**
         * 密码
         */
        private String pwd;

    }

    @PostConstruct
    public void init() {
        if (services != null) {
            serviceMap = services.stream()
                .collect(Collectors.toMap(ServiceProperties::getId, Function.identity()));
        }
    }
}
