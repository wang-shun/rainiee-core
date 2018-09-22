package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 婚姻状况
 */
public enum T6012_F02 {

	/**
	 * 未婚
	 */
	WH("未婚"),

	/**
	 * 已婚
	 */
	YH("已婚");

	protected final String chineseName;

	private T6012_F02(String chineseName) {
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
	 * @return {@link T6012_F02}
	 */
	public static final T6012_F02 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6012_F02.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
