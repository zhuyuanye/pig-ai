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

package com.pig4cloud.pig.monitor.config;

import com.pig4cloud.pig.common.security.annotation.EnablePigResourceServer;
import org.springframework.context.annotation.Configuration;

/**
 * 安全配置类：启用 Pig OAuth2 资源服务器
 * <p>
 * 通过 @EnablePigResourceServer 注解导入 Pig 的 OAuth2 资源服务器配置，
 * 包括 opaque token 内省、bearer token 提取、permit-all URL 配置等。
 * <p>
 * 需要放行的 URL 通过 security.oauth2.ignore.urls 配置，
 * 或在 Controller 方法上使用 @Inner 注解标记。
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Configuration(proxyBeanMethods = false)
@EnablePigResourceServer
public class SecuritySecureConfig {

}
