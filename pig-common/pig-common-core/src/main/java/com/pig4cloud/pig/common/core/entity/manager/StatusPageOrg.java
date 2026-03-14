/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.core.entity.manager;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 状态页组织实体类
 * <p>
 * 用于表示状态页的组织信息，一个组织可以有多个状态页
 * <p>
 * 主要功能：
 * <ul>
 *   <li>定义状态页的基本信息</li>
 *   <li>配置状态页的样式和主题</li>
 *   <li>维护组织的状态汇总</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_status_page_org")
@Schema(description = "状态页组织实体")
public class StatusPageOrg implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 * <p>
	 * 使用数据库自增策略生成的唯一标识
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(title = "主键ID", example = "87584674384")
	private Long id;

	/**
	 * 组织名称
	 * <p>
	 * 组织的显示名称
	 */
	@Schema(title = "组织名称", example = "TanCloud")
	@NotBlank
	private String name;

	/**
	 * 组织描述
	 * <p>
	 * 对组织的简要说明
	 */
	@Schema(title = "组织描述", example = "云服务监控平台")
	@NotBlank
	private String description;

	/**
	 * 组织主页URL
	 * <p>
	 * 组织官网或主页链接
	 */
	@Schema(title = "组织主页URL", example = "https://tancloud.com")
	@NotBlank
	private String home;

	/**
	 * 组织Logo URL
	 * <p>
	 * 组织Logo图片的URL地址
	 */
	@Schema(title = "组织Logo URL", example = "https://tancloud.com/logo.svg")
	@NotBlank
	private String logo;

	/**
	 * 反馈联系信息
	 * <p>
	 * 用户反馈或联系方式，如邮箱
	 */
	@Schema(title = "反馈联系信息", example = "contact@tancloud.com")
	private String feedback;

	/**
	 * 主题背景色
	 * <p>
	 * 状态页的主题背景颜色，十六进制颜色值
	 */
	@Schema(title = "主题背景色", example = "#ffffff")
	private String color;

	/**
	 * 组织当前状态
	 * <p>
	 * 组织的整体状态，根据所有组件状态汇总
	 * <ul>
	 *   <li>0 - 所有系统运行正常</li>
	 *   <li>1 - 部分系统异常</li>
	 *   <li>2 - 所有系统异常</li>
	 * </ul>
	 */
	@Schema(title = "组织状态: 0-全部正常 1-部分异常 2-全部异常", example = "0")
	private Byte state;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该组织的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom")
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该组织的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者", example = "tom")
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 组织创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000")
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 组织最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "1612198444000")
	private LocalDateTime gmtUpdate;

}
