package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否转出
 */
public enum T6038_F07 {

	/**
	 * 否
	 */
	F("否"),

	/**
	 * 是
	 */
	S("是");

	protected final String chineseName;

	private T6038_F07(String chineseName) {
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
	 * @return {@link T6038_F07}
	 */
	public static final T6038_F07 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6038_F07.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
