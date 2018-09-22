package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 有无房贷
 */
public enum T6015_F04 {

	/**
	 * 无
	 */
	W("无"),

	/**
	 * 有
	 */
	Y("有");

	protected final String chineseName;

	private T6015_F04(String chineseName) {
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
	 * @return {@link T6015_F04}
	 */
	public static final T6015_F04 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6015_F04.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
