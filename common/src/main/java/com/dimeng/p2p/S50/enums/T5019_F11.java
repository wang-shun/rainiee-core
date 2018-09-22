package com.dimeng.p2p.S50.enums;

import com.dimeng.util.StringHelper;

/**
 * 级别
 */
public enum T5019_F11 {

	/**
	 * 省级
	 */
	SHENG("省级"),

	/**
	 * 市级
	 */
	SHI("市级"),

	/**
	 * 县级
	 */
	XIAN("县级");

	protected final String chineseName;

	private T5019_F11(String chineseName) {
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
	 * @return {@link T5019_F11}
	 */
	public static final T5019_F11 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T5019_F11.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
