package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否生效
 */
public enum T7021_F08 {

	/**
	 * 是
	 */
	S("是"),

	/**
	 * 否
	 */
	F("否");

	protected final String chineseName;

	private T7021_F08(String chineseName) {
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
	 * @return {@link T7021_F08}
	 */
	public static final T7021_F08 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7021_F08.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
