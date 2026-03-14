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

package com.pig4cloud.pig.common.mybatis.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON Map 类型处理器
 * <p>
 * 用于处理数据库中存储的JSON格式字符串与Java Map对象的相互转换
 * <p>
 * 支持将数据库中的VARCHAR类型字段（存储JSON字符串）自动转换为Java的Map&lt;String, String&gt;对象，
 * 以及将Map对象序列化为JSON字符串存储到数据库
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see MappedTypes 映射的Java类型为Map&lt;String, String&gt;
 * @see MappedJdbcTypes 映射的JDBC类型为VARCHAR
 */
@MappedTypes(value = { Map.class })
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class JsonMapTypeHandler extends BaseTypeHandler<Map<String, String>> {

	/**
	 * JSON序列化和反序列化工具
	 */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * 设置非空参数到PreparedStatement中
	 * <p>
	 * 将Map对象序列化为JSON字符串后设置到PreparedStatement的指定位置
	 *
	 * @param ps        PreparedStatement对象
	 * @param i         参数位置
	 * @param parameter Map类型参数值
	 * @param jdbcType  JDBC类型
	 * @throws SQLException 数据库操作异常
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Map<String, String> parameter, JdbcType jdbcType)
			throws SQLException {
		try {
			ps.setString(i, OBJECT_MAPPER.writeValueAsString(parameter));
		}
		catch (JsonProcessingException e) {
			throw new SQLException("Error converting Map to JSON string", e);
		}
	}

	/**
	 * 根据列名从ResultSet中获取可空的Map结果
	 * <p>
	 * 从结果集中获取指定列名的JSON字符串，并反序列化为Map对象
	 *
	 * @param rs         ResultSet对象
	 * @param columnName 列名
	 * @return Map对象，如果JSON为空或解析失败则返回空Map
	 * @throws SQLException 数据库访问异常
	 */
	@Override
	@SneakyThrows
	public Map<String, String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String json = rs.getString(columnName);
		return parseJson(json);
	}

	/**
	 * 根据列位置从ResultSet中获取可空的Map结果
	 * <p>
	 * 从结果集中获取指定列位置的JSON字符串，并反序列化为Map对象
	 *
	 * @param rs          ResultSet对象
	 * @param columnIndex 列索引位置
	 * @return Map对象，如果JSON为空或解析失败则返回空Map
	 * @throws SQLException 数据库访问异常
	 */
	@Override
	@SneakyThrows
	public Map<String, String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String json = rs.getString(columnIndex);
		return parseJson(json);
	}

	/**
	 * 从CallableStatement中获取可空的Map结果
	 * <p>
	 * 从存储过程调用语句中获取指定列位置的JSON字符串，并反序列化为Map对象
	 *
	 * @param cs          CallableStatement对象
	 * @param columnIndex 列索引位置
	 * @return Map对象，如果JSON为空或解析失败则返回空Map
	 * @throws SQLException 数据库访问异常
	 */
	@Override
	@SneakyThrows
	public Map<String, String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String json = cs.getString(columnIndex);
		return parseJson(json);
	}

	/**
	 * 解析JSON字符串为Map对象
	 * <p>
	 * 将数据库中的JSON字符串反序列化为Map对象，如果字符串为空或解析失败则返回空Map
	 *
	 * @param json JSON格式字符串
	 * @return Map对象，不为null
	 */
	private Map<String, String> parseJson(String json) {
		if (json == null || json.trim().isEmpty()) {
			return new HashMap<>();
		}
		try {
			return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, String>>() {
			});
		}
		catch (JsonProcessingException e) {
			return new HashMap<>();
		}
	}

}
