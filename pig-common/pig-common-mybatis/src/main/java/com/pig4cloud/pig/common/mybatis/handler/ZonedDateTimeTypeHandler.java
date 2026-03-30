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

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ZonedDateTime 类型处理器
 * <p>
 * 用于处理数据库中字符串与 Java ZonedDateTime 的相互转换。
 * 告警静默规则和通知规则中的时间段配置使用此类型。
 *
 * @author pig4cloud
 */
public class ZonedDateTimeTypeHandler extends BaseTypeHandler<ZonedDateTime> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, ZonedDateTime parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
	}

	@Override
	public ZonedDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return parse(rs.getString(columnName));
	}

	@Override
	public ZonedDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return parse(rs.getString(columnIndex));
	}

	@Override
	public ZonedDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return parse(cs.getString(columnIndex));
	}

	private ZonedDateTime parse(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		try {
			return ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		}
		catch (Exception e) {
			return null;
		}
	}

}
