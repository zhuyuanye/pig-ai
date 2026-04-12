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

package com.pig4cloud.pig.alerter;

import com.pig4cloud.pig.common.core.constants.ConfigConstants;
import com.pig4cloud.pig.common.core.constants.SignConstants;
import com.pig4cloud.pig.common.feign.annotation.EnablePigFeignClients;
import com.pig4cloud.pig.common.security.annotation.EnablePigResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 告警服务启动类
 * <p>
 * 基于 HertzBeat 告警模块迁移，提供告警规则定义、告警触发、
 * 告警通知（邮件/钉钉/微信/飞书/Telegram/Webhook 等）、
 * 告警静默、告警抑制、告警收敛等功能。
 * <p>
 * 使用 MyBatis Plus 作为 ORM 框架，Pig OAuth2 作为安全体系。
 *
 * @author pig4cloud
 * @date 2025/06/01
 */
@EnableDiscoveryClient
@EnablePigFeignClients
@EnablePigResourceServer
@SpringBootApplication
@MapperScan({"com.pig4cloud.pig.common.alert.mapper", "com.pig4cloud.pig.common.base.mapper"})
@ComponentScan(
		basePackages = ConfigConstants.PkgConstant.PKG
				+ SignConstants.DOT
				+ ConfigConstants.FunctionModuleConstants.CORE
)
@EnableAsync
@EnableScheduling
public class PigAlerterApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigAlerterApplication.class, args);
	}

}
