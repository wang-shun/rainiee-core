package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否受益
 */
public enum T7047_F05 {

	/**
	 * 否
	 */
	F("否"),

	/**
	 * 是
	 */
	S("是");

	protected final String chineseName;

	private T7047_F05(String chineseName) {
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
	 * @return {@link T7047_F05}
	 */
	public static final T7047_F05 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7047_F05.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
