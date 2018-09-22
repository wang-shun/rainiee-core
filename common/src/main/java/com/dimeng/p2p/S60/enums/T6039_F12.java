package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否发布
 */
public enum T6039_F12 {

	/**
	 * 是
	 */
	S("是"),

	/**
	 * 否
	 */
	F("否");

	protected final String chineseName;

	private T6039_F12(String chineseName) {
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
	 * @return {@link T6039_F12}
	 */
	public static final T6039_F12 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6039_F12.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
