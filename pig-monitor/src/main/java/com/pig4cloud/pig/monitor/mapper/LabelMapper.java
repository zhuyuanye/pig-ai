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

package com.pig4cloud.pig.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.common.core.entity.manager.Label;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 标签 Mapper 接口
 * <p>
 * 负责标签数据的数据库操作，继承MyBatis Plus的BaseMapper提供基础CRUD功能
 * <p>
 * 主要功能：
 * <ul>
 *   <li>标签的基础增删改查操作</li>
 *   <li>根据ID集合批量删除标签</li>
 *   <li>根据名称和值查询标签</li>
 *   <li>管理监控任务的标签分类和标识</li>
 * </ul>
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see BaseMapper MyBatis Plus基础Mapper接口
 * @see Label 标签实体类
 */
@Mapper
public interface LabelMapper extends BaseMapper<Label> {

	/**
	 * 根据ID集合批量删除标签
	 * <p>
	 * 批量删除指定ID的标签，通常用于标签清理或批量管理操作
	 *
	 * @param ids 标签ID集合
	 */
	void deleteLabelsByIdIn(@Param("ids") Set<Long> ids);

	/**
	 * 根据名称和值查询标签
	 * <p>
	 * 通过标签的名称和值组合查询对应的标签详情
	 * <p>
	 * 注意：标签由名称和值组成，相同名称但不同值的标签被视为不同的标签
	 *
	 * @param name  标签名称
	 * @param value 标签值
	 * @return 标签对象，如果不存在则返回null
	 */
	Label findLabelByNameAndTagValue(@Param("name") String name, @Param("value") String value);

}
