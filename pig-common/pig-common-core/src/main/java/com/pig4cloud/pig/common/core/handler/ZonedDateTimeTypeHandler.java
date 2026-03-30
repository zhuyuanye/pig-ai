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

package com.pig4cloud.pig.common.core.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ZonedDateTime 绫诲瀷澶勭悊鍣?
 * <p>
 * 鐢ㄤ簬澶勭悊鏁版嵁搴撲腑瀛楃涓蹭笌 Java ZonedDateTime 鐨勭浉浜掕浆鎹€?
 * 鍛婅闈欓粯瑙勫垯鍜岄€氱煡瑙勫垯涓殑鏃堕棿娈甸厤缃娇鐢ㄦ绫诲瀷銆?
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

