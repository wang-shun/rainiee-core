package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 借款标类型
 */
public enum T6036_F19 {

	QYDYDB("QYDYDB"), QYXY("QYXY"), GRDYDB("GRDYDB"), GRXY("GRXY"), XYDB("XYDB"), SDRZ(
			"SDRZ"), SYD("SYD"), XJD("XJD");

	protected final String chineseName;

	private T6036_F19(String chineseName) {
		this.chineseName = chineseName;
	}

	/**
	 * 获取中文名称.
	 * 
	 * @return {@link String}
	 */
	public String getChineseName() {
		return chineseName;
	}

	/**
	 * 解析字符串.
	 * 
	 * @return {@link T6036_F19}
	 */
	public static final T6036_F19 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6036_F19.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
