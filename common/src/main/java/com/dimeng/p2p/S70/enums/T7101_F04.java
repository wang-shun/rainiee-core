package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 认证结果
 */
public enum T7101_F04 {

	/**
	 * 认证失败
	 */
	SB("认证失败"),

	/**
	 * 认证通过
	 */
	TG("认证通过");

	protected final String chineseName;

	private T7101_F04(String chineseName) {
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
	 * @return {@link T7101_F04}
	 */
	public static final T7101_F04 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7101_F04.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
