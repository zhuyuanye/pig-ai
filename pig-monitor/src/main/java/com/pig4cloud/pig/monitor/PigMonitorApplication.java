/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.monitor;

import com.pig4cloud.pig.common.feign.annotation.EnablePigFeignClients;
import com.pig4cloud.pig.monitor.nativex.HertzbeatRuntimeHintsRegistrar;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;

/**
 * 监控中心应用启动类
 * <p>
 * 使用 MyBatis-Plus 和 JPA 混合模式作为数据访问层ORM框架
 * <p>
 * 主要配置：
 * <ul>
 *   <li>启用Spring Boot Admin服务监控</li>
 *   <li>启用服务发现客户端（Nacos）</li>
 *   <li>配置MyBatis Mapper扫描路径（pig-monitor模块使用）</li>
 *   <li>配置JPA Repository扫描路径（支持pig-common模块的JPA Dao）</li>
 *   <li>启用JPA审计功能</li>
 *   <li>启用异步任务和定时调度</li>
 * </ul>
 *
 * @author lengleng
 * @date 2018/06/21
 */
@EnableAdminServer
@EnableDiscoveryClient
@EnablePigFeignClients
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.pig4cloud.pig.common.base", "com.pig4cloud.pig.common.alert"})
@EntityScan(basePackages = {"com.pig4cloud"})
@MapperScan("com.pig4cloud.pig.monitor.mapper")
@ComponentScan(basePackages = {"com.pig4cloud"})
@ConfigurationPropertiesScan(basePackages = {"com.pig4cloud"})
@ImportRuntimeHints(HertzbeatRuntimeHintsRegistrar.class)
@EnableAsync
@EnableScheduling
public class PigMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigMonitorApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.setProperty("jdk.jndi.object.factoriesFilter", "!com.zaxxer.hikari.HikariJNDIFactory");
	}
}
