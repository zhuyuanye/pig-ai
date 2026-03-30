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

package com.pig4cloud.pig.common.mybatis.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON Byte List 类型处理器
 * <p>
 * 用于处理数据库中 JSON 数组字符串与 Java List&lt;Byte&gt; 的相互转换。
 * 例如告警静默规则中的星期几列表 [1,2,3,4,5]。
 *
 * @author pig4cloud
 */
public class JsonByteListTypeHandler extends BaseTypeHandler<List<Byte>> {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, List<Byte> parameter, JdbcType jdbcType)
			throws SQLException {
		try {
			ps.setString(i, OBJECT_MAPPER.writeValueAsString(parameter));
		}
		catch (JsonProcessingException e) {
			throw new SQLException("Error converting List<Byte> to JSON string", e);
		}
	}

	@Override
	@SneakyThrows
	public List<Byte> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return parseJson(rs.getString(columnName));
	}

	@Override
	@SneakyThrows
	public List<Byte> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return parseJson(rs.getString(columnIndex));
	}

	@Override
	@SneakyThrows
	public List<Byte> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return parseJson(cs.getString(columnIndex));
	}

	private List<Byte> parseJson(String json) {
		if (json == null || json.trim().isEmpty()) {
			return new ArrayList<>();
		}
		try {
			return OBJECT_MAPPER.readValue(json, new TypeReference<List<Byte>>() {
			});
		}
		catch (JsonProcessingException e) {
			return new ArrayList<>();
		}
	}

}
