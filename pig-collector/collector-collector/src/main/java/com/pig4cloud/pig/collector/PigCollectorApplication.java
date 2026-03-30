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

package com.pig4cloud.pig.collector;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 采集器服务启动类
 * <p>
 * 基于 HertzBeat 采集器模块迁移，提供多协议数据采集能力，
 * 支持 HTTP/SSH/SNMP/JDBC/Redis/Kafka/MongoDB 等 20+ 种采集协议。
 * <p>
 * 通过 Netty 长连接与 pig-monitor（管理端）通信，
 * 接收采集任务并上报采集数据。
 * <p>
 * 注册到 Nacos 实现服务发现，支持水平扩展部署多个采集器实例。
 *
 * @author pig4cloud
 */
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.pig4cloud.pig"})
@ConfigurationPropertiesScan(basePackages = {"com.pig4cloud.pig"})
@SpringBootApplication
public class PigCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigCollectorApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.setProperty("jdk.jndi.object.factoriesFilter", "!com.zaxxer.hikari.HikariJNDIFactory");
	}

}
