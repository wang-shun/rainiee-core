package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否第一次登录修改密码
 */
public enum T7011_F09 {

	/**
	 * 否
	 */
	F("否"),

	/**
	 * 是
	 */
	S("是");

	protected final String chineseName;

	private T7011_F09(String chineseName) {
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
	 * @return {@link T7011_F09}
	 */
	public static final T7011_F09 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7011_F09.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
