package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否第一次登录修改密码
 */
public enum T6010_1_F03 {

	/**
	 * 否
	 */
	F("否"),

	/**
	 * 是
	 */
	S("是");

	protected final String chineseName;

	private T6010_1_F03(String chineseName) {
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
	 * @return {@link T6010_1_F03}
	 */
	public static final T6010_1_F03 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6010_1_F03.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
