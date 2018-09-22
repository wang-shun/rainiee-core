package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否逾期
 */
public enum T6036_F39 {

	/**
	 * 严重逾期
	 */
	YZYQ("严重逾期"),

	/**
	 * 是(逾期)
	 */
	S("是(逾期)"),

	/**
	 * 否
	 */
	F("否");

	protected final String chineseName;

	private T6036_F39(String chineseName) {
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
	 * @return {@link T6036_F39}
	 */
	public static final T6036_F39 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6036_F39.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
