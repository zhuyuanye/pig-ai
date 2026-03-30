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

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * 公告实体类
 * <p>
 * 用于存储系统公告信息，可以关联到特定的监控任务
 * <p>
 * 主要功能：
 * <ul>
 *   <li>系统公告的创建和管理</li>
 *   <li>公告与监控任务的关联</li>
 *   <li>公告字段配置管理</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hzb_bulletin")
@Schema(description = "公告实体")
public class Bulletin implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 * <p>
	 * 使用数据库自增策略生成的唯一标识
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(description = "公告ID", example = "1")
	private Long id;

	/**
	 * 公告名称
	 * <p>
	 * 公告的显示名称，用于标识和区分不同的公告
	 */
	@Schema(description = "公告名称", example = "Bulletin1", accessMode = READ_WRITE)
	private String name;

	/**
	 * 监控任务ID列表
	 * <p>
	 * 关联到此公告的监控任务ID集合，以JSON数组格式存储
	 */
	@TableField(value = "monitor_ids", typeHandler = com.pig4cloud.pig.common.core.handler.JsonLongListTypeHandler.class)
	@Schema(description = "监控任务ID列表", example = "[1,2,3]")
	private List<Long> monitorIds;

	/**
	 * 监控类型
	 * <p>
	 * 公告关联的监控类型，如jvm、tomcat、mysql等
	 */
	@Schema(description = "监控类型", example = "jvm", accessMode = READ_WRITE)
	private String app;

	/**
	 * 监控字段
	 * <p>
	 * 用于定义公告关注的监控字段，以JSON格式存储
	 * <p>
	 * 示例：{"fields": ["heapMemory", "cpuUsage"]}
	 */
	@TableField(value = "fields", typeHandler = com.pig4cloud.pig.common.core.handler.JsonMapListTypeHandler.class)
	@Schema(description = "监控字段配置")
	private Map<String, List<String>> fields;

	/**
	 * 创建者
	 * <p>
	 * 记录创建该公告的用户名
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者", example = "tom", accessMode = READ_WRITE)
	private String creator;

	/**
	 * 修改者
	 * <p>
	 * 记录最后修改该公告的用户名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改者", example = "tom", accessMode = READ_WRITE)
	private String modifier;

	/**
	 * 创建时间
	 * <p>
	 * 公告创建的时间戳
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间", example = "2024-07-02T20:09:34.903217", accessMode = READ_WRITE)
	private LocalDateTime gmtCreate;

	/**
	 * 修改时间
	 * <p>
	 * 公告最后修改的时间戳
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间", example = "2024-07-02T20:09:34.903217", accessMode = READ_WRITE)
	private LocalDateTime gmtUpdate;

}
