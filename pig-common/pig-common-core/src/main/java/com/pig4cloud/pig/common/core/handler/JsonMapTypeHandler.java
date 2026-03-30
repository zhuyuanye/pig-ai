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

package com.pig4cloud.pig.common.core.handler;

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
 * JSON Map 绫诲瀷澶勭悊鍣?
 * <p>
 * 鐢ㄤ簬澶勭悊鏁版嵁搴撲腑瀛樺偍鐨凧SON鏍煎紡瀛楃涓蹭笌Java Map瀵硅薄鐨勭浉浜掕浆鎹?
 * <p>
 * 鏀寔灏嗘暟鎹簱涓殑VARCHAR绫诲瀷瀛楁锛堝瓨鍌↗SON瀛楃涓诧級鑷姩杞崲涓篔ava鐨凪ap&lt;String, String&gt;瀵硅薄锛?
 * 浠ュ強灏哅ap瀵硅薄搴忓垪鍖栦负JSON瀛楃涓插瓨鍌ㄥ埌鏁版嵁搴?
 *
 * @author HertzBeat
 * @since 1.0.0
 * @see MappedTypes 鏄犲皠鐨凧ava绫诲瀷涓篗ap&lt;String, String&gt;
 * @see MappedJdbcTypes 鏄犲皠鐨凧DBC绫诲瀷涓篤ARCHAR
 */
@MappedTypes(value = { Map.class })
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class JsonMapTypeHandler extends BaseTypeHandler<Map<String, String>> {

	/**
	 * JSON搴忓垪鍖栧拰鍙嶅簭鍒楀寲宸ュ叿
	 */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * 璁剧疆闈炵┖鍙傛暟鍒癙reparedStatement涓?
	 * <p>
	 * 灏哅ap瀵硅薄搴忓垪鍖栦负JSON瀛楃涓插悗璁剧疆鍒癙reparedStatement鐨勬寚瀹氫綅缃?
	 *
	 * @param ps        PreparedStatement瀵硅薄
	 * @param i         鍙傛暟浣嶇疆
	 * @param parameter Map绫诲瀷鍙傛暟鍊?
	 * @param jdbcType  JDBC绫诲瀷
	 * @throws SQLException 鏁版嵁搴撴搷浣滃紓甯?
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
	 * 鏍规嵁鍒楀悕浠嶳esultSet涓幏鍙栧彲绌虹殑Map缁撴灉
	 * <p>
	 * 浠庣粨鏋滈泦涓幏鍙栨寚瀹氬垪鍚嶇殑JSON瀛楃涓诧紝骞跺弽搴忓垪鍖栦负Map瀵硅薄
	 *
	 * @param rs         ResultSet瀵硅薄
	 * @param columnName 鍒楀悕
	 * @return Map瀵硅薄锛屽鏋淛SON涓虹┖鎴栬В鏋愬け璐ュ垯杩斿洖绌篗ap
	 * @throws SQLException 鏁版嵁搴撹闂紓甯?
	 */
	@Override
	@SneakyThrows
	public Map<String, String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String json = rs.getString(columnName);
		return parseJson(json);
	}

	/**
	 * 鏍规嵁鍒椾綅缃粠ResultSet涓幏鍙栧彲绌虹殑Map缁撴灉
	 * <p>
	 * 浠庣粨鏋滈泦涓幏鍙栨寚瀹氬垪浣嶇疆鐨凧SON瀛楃涓诧紝骞跺弽搴忓垪鍖栦负Map瀵硅薄
	 *
	 * @param rs          ResultSet瀵硅薄
	 * @param columnIndex 鍒楃储寮曚綅缃?
	 * @return Map瀵硅薄锛屽鏋淛SON涓虹┖鎴栬В鏋愬け璐ュ垯杩斿洖绌篗ap
	 * @throws SQLException 鏁版嵁搴撹闂紓甯?
	 */
	@Override
	@SneakyThrows
	public Map<String, String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String json = rs.getString(columnIndex);
		return parseJson(json);
	}

	/**
	 * 浠嶤allableStatement涓幏鍙栧彲绌虹殑Map缁撴灉
	 * <p>
	 * 浠庡瓨鍌ㄨ繃绋嬭皟鐢ㄨ鍙ヤ腑鑾峰彇鎸囧畾鍒椾綅缃殑JSON瀛楃涓诧紝骞跺弽搴忓垪鍖栦负Map瀵硅薄
	 *
	 * @param cs          CallableStatement瀵硅薄
	 * @param columnIndex 鍒楃储寮曚綅缃?
	 * @return Map瀵硅薄锛屽鏋淛SON涓虹┖鎴栬В鏋愬け璐ュ垯杩斿洖绌篗ap
	 * @throws SQLException 鏁版嵁搴撹闂紓甯?
	 */
	@Override
	@SneakyThrows
	public Map<String, String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String json = cs.getString(columnIndex);
		return parseJson(json);
	}

	/**
	 * 瑙ｆ瀽JSON瀛楃涓蹭负Map瀵硅薄
	 * <p>
	 * 灏嗘暟鎹簱涓殑JSON瀛楃涓插弽搴忓垪鍖栦负Map瀵硅薄锛屽鏋滃瓧绗︿覆涓虹┖鎴栬В鏋愬け璐ュ垯杩斿洖绌篗ap
	 *
	 * @param json JSON鏍煎紡瀛楃涓?
	 * @return Map瀵硅薄锛屼笉涓簄ull
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

