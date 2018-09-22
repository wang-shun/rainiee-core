package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否马赛克
 */
public enum T6216_F05 {

	F("F"), S("S");

	protected final String chineseName;

	private T6216_F05(String chineseName) {
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
	 * @return {@link T6216_F05}
	 */
	public static final T6216_F05 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6216_F05.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
