package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否投资可用
 */
public enum T6111_F12 {

	/**
	 * 否
	 */
	F("否"),

	/**
	 * 是
	 */
	S("是");

	protected final String chineseName;

	private T6111_F12(String chineseName) {
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
	 * @return {@link T6111_F12}
	 */
	public static final T6111_F12 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6111_F12.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
