package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否提前还款
 */
public enum T6041_F13 {

	S("S"), F("F");

	protected final String chineseName;

	private T6041_F13(String chineseName) {
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
	 * @return {@link T6041_F13}
	 */
	public static final T6041_F13 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6041_F13.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
