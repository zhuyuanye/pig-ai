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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 监控参数定义实体类
 * <p>
 * 用于定义监控任务的参数结构、验证规则和显示配置
 * <p>
 * 主要功能：
 * <ul>
 *   <li>定义监控类型的参数结构</li>
 *   <li>配置参数的验证规则和默认值</li>
 *   <li>设置参数的UI显示属性</li>
 *   <li>支持参数之间的依赖关系</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_param_define")
@Schema(description = "参数结构定义实体")
public class ParamDefine implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 参数结构ID
	 * <p>
	 * 使用数据库自增策略生成的唯一标识
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(title = "参数结构ID", example = "87584674384", accessMode = READ_ONLY)
	private Long id;

	/**
	 * 监控应用类型名称
	 * <p>
	 * 参数定义所属的监控类型，如http、ping、mysql等
	 */
	@Schema(title = "监控类型", example = "http", accessMode = READ_WRITE)
	private String app;

	/**
	 * 参数字段外部显示名称
	 * <p>
	 * 参数字段的国际化显示名称，支持多语言
	 * <p>
	 * 示例：{"zh-CN": "端口", "en-US": "Port"}
	 */
	@TableField(typeHandler = "com.pig4cloud.pig.common.mybatis.handler.JsonMapTypeHandler")
	@Schema(description = "参数字段国际化显示名称", example = "{zh-CN: '端口', en-US: 'Port'}", accessMode = READ_WRITE)
	private Map<String, String> name;

	/**
	 * 参数字段标识符
	 * <p>
	 * 参数的唯一标识符，如port、url、username等
	 */
	@Schema(title = "参数字段标识符", example = "port", accessMode = READ_WRITE)
	private String field;

	/**
	 * 字段类型
	 * <p>
	 * 定义参数的输入类型，主要映射HTML input标签的type属性
	 * <p>
	 * 常用类型：
	 * <ul>
	 *   <li>text - 文本输入框</li>
	 *   <li>number - 数字输入框</li>
	 *   <li>password - 密码输入框</li>
	 *   <li>radio - 单选框</li>
	 *   <li>checkbox - 复选框</li>
	 *   <li>key-value - 键值对输入</li>
	 * </ul>
	 */
	@Schema(title = "字段类型", example = "number", accessMode = READ_WRITE)
	private String type;

	/**
	 * 是否必填
	 * <p>
	 * 定义参数是否为必填项
	 * <ul>
	 *   <li>true - 必填参数</li>
	 *   <li>false - 可选参数</li>
	 * </ul>
	 */
	@Schema(title = "是否必填", example = "true", accessMode = READ_WRITE)
	private Boolean required = false;

	/**
	 * 参数默认值
	 * <p>
	 * 参数的默认值，用户未输入时使用此值
	 */
	@Schema(title = "参数默认值", example = "8080", accessMode = READ_WRITE)
	private String defaultValue;

	/**
	 * 参数输入框提示信息
	 * <p>
	 * 显示在输入框中的提示文本，引导用户输入
	 */
	@Schema(title = "参数输入框提示信息", example = "请输入端口号", accessMode = READ_WRITE)
	private String placeholder;

	/**
	 * 参数范围
	 * <p>
	 * 当type为number时，使用range表示数值的有效范围
	 * <p>
	 * 示例：0-65535 表示端口范围
	 */
	@TableField("param_range")
	@Schema(title = "数值范围", example = "0-65535", accessMode = READ_WRITE)
	private String range;

	/**
	 * 参数长度限制
	 * <p>
	 * 当type为text时，使用limit表示字符串的最大长度
	 */
	@TableField("param_limit")
	@Schema(title = "字符串长度限制", example = "30", accessMode = READ_WRITE)
	private Short limit;

	/**
	 * 参数选项列表
	 * <p>
	 * 当type为radio或checkbox时，options表示可选值列表
	 * <p>
	 * 示例格式：[{"label":"选项1","value":"value1"},{"label":"选项2","value":"value2"}]
	 * <ul>
	 *   <li>label - 显示的标签文本</li>
	 *   <li>value - 实际的值</li>
	 * </ul>
	 */
	@TableField(value = "param_options", typeHandler = "com.pig4cloud.pig.common.mybatis.handler.JsonOptionListTypeHandler")
	@Schema(description = "单选框/复选框的可选值列表", example = "[{\"label\":\"选项1\",\"value\":\"value1\"}]",
			accessMode = READ_WRITE)
	private List<Object> options;

	/**
	 * 键别名
	 * <p>
	 * 当type为key-value时，表示键的别名描述
	 */
	@Schema(title = "键别名", example = "名称", accessMode = READ_WRITE)
	private String keyAlias;

	/**
	 * 值别名
	 * <p>
	 * 当type为key-value时，表示值的别名描述
	 */
	@Schema(title = "值别名", example = "值", accessMode = READ_WRITE)
	private String valueAlias;

	/**
	 * 是否为高级隐藏参数
	 * <p>
	 * 定义参数是否为高级配置，默认隐藏
	 * <ul>
	 *   <li>true - 高级参数，默认隐藏</li>
	 *   <li>false - 普通参数，默认显示</li>
	 * </ul>
	 */
	@Schema(title = "是否为高级隐藏参数", example = "true", accessMode = READ_WRITE)
	private Boolean hide = false;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该参数定义的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom", accessMode = READ_ONLY)
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该参数定义的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者", example = "tom", accessMode = READ_ONLY)
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 参数定义创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "1612198922000", accessMode = READ_ONLY)
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 参数定义最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "1612198444000", accessMode = READ_ONLY)
	private LocalDateTime gmtUpdate;

	/**
	 * 参数依赖关系
	 * <p>
	 * 定义该参数依赖于其他参数的哪些值
	 * <p>
	 * 示例：{"field":["value1","value2"]} 表示当field参数的值为value1或value2时，此参数才显示
	 */
	@TableField(typeHandler = "com.pig4cloud.pig.common.mybatis.handler.JsonMapTypeHandler")
	@Schema(title = "参数依赖关系", example = "{\"field\":[\"value1\",\"value2\"]}", accessMode = READ_WRITE)
	private Map<String, List<Object>> depend;

	/**
	 * 参数选项配置类
	 * <p>
	 * 用于定义单选框和复选框的选项
	 */
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class Option {

		/**
		 * 选项显示标签
		 */
		private String label;

		/**
		 * 选项实际值
		 */
		private String value;

	}

}
