package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 类型
 */
public enum T7043_F05 {

	SDRZ("SDRZ"), JGDB("JGDB"), XYRZ("XYRZ");

	protected final String chineseName;

	private T7043_F05(String chineseName) {
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
	 * @return {@link T7043_F05}
	 */
	public static final T7043_F05 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7043_F05.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
