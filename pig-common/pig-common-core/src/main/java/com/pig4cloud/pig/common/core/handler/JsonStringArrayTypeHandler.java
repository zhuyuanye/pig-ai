package com.pig4cloud.pig.common.core.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis 瀛楃涓叉暟缁勭被鍨嬪鐞嗗櫒锛岀敤浜庢暟鎹簱VARCHAR绫诲瀷涓嶫ava瀛楃涓叉暟缁勭殑鐩镐簰杞崲
 *
 * @author lengleng
 * @date 2025/05/31
 * @see MappedTypes 鎸囧畾澶勭悊鐨凧ava绫诲瀷
 * @see MappedJdbcTypes 鎸囧畾澶勭悊鐨凧DBC绫诲瀷
 */
@MappedTypes(value = { String[].class })
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class JsonStringArrayTypeHandler extends BaseTypeHandler<String[]> {

	/**
	 * 璁剧疆闈炵┖鍙傛暟鍒癙reparedStatement
	 * @param ps PreparedStatement瀵硅薄
	 * @param i 鍙傛暟浣嶇疆
	 * @param parameter 瀛楃涓叉暟缁勫弬鏁?
	 * @param jdbcType JDBC绫诲瀷
	 * @throws SQLException 鏁版嵁搴撴搷浣滃紓甯?
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, ArrayUtil.join(parameter, StrUtil.COMMA));
	}

	/**
	 * 浠嶳esultSet涓幏鍙栨寚瀹氬垪鍚嶇殑瀛楃涓叉暟缁勭粨鏋滐紝鍏佽涓簄ull
	 * @param rs 缁撴灉闆?
	 * @param columnName 鍒楀悕
	 * @return 杞崲鍚庣殑瀛楃涓叉暟缁勶紝鍙兘涓簄ull
	 * @throws SQLException 鏁版嵁搴撹闂敊璇椂鎶涘嚭
	 */
	@Override
	@SneakyThrows
	public String[] getNullableResult(ResultSet rs, String columnName) {
		String reString = rs.getString(columnName);
		return Convert.toStrArray(reString);
	}

	/**
	 * 浠嶳esultSet涓幏鍙栨寚瀹氬垪鐨勫彲绌哄瓧绗︿覆鏁扮粍缁撴灉
	 * @param rs 缁撴灉闆?
	 * @param columnIndex 鍒楃储寮?
	 * @return 杞崲鍚庣殑瀛楃涓叉暟缁勶紝鍙兘涓簄ull
	 * @throws SQLException 鏁版嵁搴撹闂敊璇椂鎶涘嚭
	 */
	@Override
	@SneakyThrows
	public String[] getNullableResult(ResultSet rs, int columnIndex) {
		String reString = rs.getString(columnIndex);
		return Convert.toStrArray(reString);
	}

	/**
	 * 浠嶤allableStatement涓幏鍙栨寚瀹氬垪鐨勫彲绌哄瓧绗︿覆缁撴灉骞惰浆鎹负瀛楃涓叉暟缁?
	 * @param cs CallableStatement瀵硅薄
	 * @param columnIndex 鍒楃储寮?
	 * @return 杞崲鍚庣殑瀛楃涓叉暟缁?
	 * @throws Exception 濡傛灉鎿嶄綔杩囩▼涓彂鐢熼敊璇?
	 */
	@Override
	@SneakyThrows
	public String[] getNullableResult(CallableStatement cs, int columnIndex) {
		String reString = cs.getString(columnIndex);
		return Convert.toStrArray(reString);
	}

}

