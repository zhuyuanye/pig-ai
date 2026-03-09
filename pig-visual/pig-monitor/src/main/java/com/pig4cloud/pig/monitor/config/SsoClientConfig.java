//package com.pig4cloud.pig.monitor.config;
//
//import com.teligen.sso.client.webSSO.client.ClientSessionListener;
//import com.teligen.sso.client.webSSO.client.SslClientFilter;
//import jakarta.servlet.Filter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Collections;
//
//@Configuration
//public class SsoClientConfig {
//
//    // 注册监听器
//    @Bean
//    public ServletListenerRegistrationBean<ClientSessionListener> clientSessionListener() {
//        return new ServletListenerRegistrationBean<>(new ClientSessionListener());
//    }
//
//    // 注册过滤器
//    @Bean
//    public FilterRegistrationBean<Filter> sslClientFilter() {
//        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
//
//        // 创建过滤器实例
//        SslClientFilter filter = new SslClientFilter();
//
//        // 设置初始化参数
//        registration.setInitParameters(Collections.singletonMap(
//            "ssoClientConfigFilePath", "ssoClient.properties"
//        ));
//
//        // 配置过滤器
//        registration.setFilter(filter);
//        registration.addUrlPatterns("*");  // 修正为正确的URL模式
//        registration.setName("sslClientFilter");
//        registration.setOrder(1);  // 设置过滤器顺序
//
//        return registration;
//    }
//}
