package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否是第三方机构
 */
public enum T6010_2_F02 {

	S("S"), F("F");

	protected final String chineseName;

	private T6010_2_F02(String chineseName) {
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
	 * @return {@link T6010_2_F02}
	 */
	public static final T6010_2_F02 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6010_2_F02.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
