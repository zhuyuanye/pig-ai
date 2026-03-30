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
 * MyBatis 闀挎暣鍨嬫暟缁勪笌瀛楃涓茬被鍨嬭浆鎹㈠鐞嗗櫒
 * <p>
 * 瀹炵幇鏁版嵁搴揤ARCHAR绫诲瀷涓嶫ava Long鏁扮粍绫诲瀷鐨勭浉浜掕浆鎹?
 *
 * @author lengleng
 * @date 2025/05/31
 * @see MappedTypes 鏄犲皠鐨凧ava绫诲瀷涓篖ong[]
 * @see MappedJdbcTypes 鏄犲皠鐨凧DBC绫诲瀷涓篤ARCHAR
 */
@MappedTypes(value = { Long[].class })
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class JsonLongArrayTypeHandler extends BaseTypeHandler<Long[]> {

	/**
	 * 璁剧疆闈炵┖鍙傛暟鍒癙reparedStatement涓?
	 * @param ps PreparedStatement瀵硅薄
	 * @param i 鍙傛暟浣嶇疆
	 * @param parameter 闀挎暣鍨嬫暟缁勫弬鏁?
	 * @param jdbcType JDBC绫诲瀷
	 * @throws SQLException 鏁版嵁搴撴搷浣滃紓甯?
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, ArrayUtil.join(parameter, StrUtil.COMMA));
	}

	/**
	 * 浠嶳esultSet涓幏鍙栨寚瀹氬垪鍚嶇殑闀挎暣鍨嬫暟缁勭粨鏋?
	 * @param rs 缁撴灉闆?
	 * @param columnName 鍒楀悕
	 * @return 杞崲鍚庣殑闀挎暣鍨嬫暟缁勶紝鍙兘涓簄ull
	 * @throws SQLException 鏁版嵁搴撹闂敊璇椂鎶涘嚭
	 */
	@Override
	@SneakyThrows
	public Long[] getNullableResult(ResultSet rs, String columnName) {
		String reString = rs.getString(columnName);
		return Convert.toLongArray(reString);
	}

	/**
	 * 浠嶳esultSet涓幏鍙栨寚瀹氬垪鐨勯暱鏁村瀷鏁扮粍
	 * @param rs 缁撴灉闆?
	 * @param columnIndex 鍒楃储寮?
	 * @return 闀挎暣鍨嬫暟缁勶紝鍙兘涓簄ull
	 * @throws SQLException 鏁版嵁搴撹闂敊璇椂鎶涘嚭
	 */
	@Override
	@SneakyThrows
	public Long[] getNullableResult(ResultSet rs, int columnIndex) {
		String reString = rs.getString(columnIndex);
		return Convert.toLongArray(reString);
	}

	/**
	 * 浠嶤allableStatement涓幏鍙栨寚瀹氬垪鐨勯暱鏁村瀷鏁扮粍
	 * @param cs CallableStatement瀵硅薄
	 * @param columnIndex 鍒楃储寮?
	 * @return 杞崲鍚庣殑闀挎暣鍨嬫暟缁勶紝鍙兘涓簄ull
	 * @throws Exception 鏁版嵁搴撹闂嚭閿欐垨杞崲寮傚父鏃舵姏鍑?
	 */
	@Override
	@SneakyThrows
	public Long[] getNullableResult(CallableStatement cs, int columnIndex) {
		String reString = cs.getString(columnIndex);
		return Convert.toLongArray(reString);
	}

}

