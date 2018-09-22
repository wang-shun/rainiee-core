package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 还款方式
 */
public enum T6036_F11 {
	/**
	 * 一次性还本付息
	 */
	YCXHBFX("一次性还本付息"),
	/**
	 * 每月还息，到期还本
	 */
	MYHXDQHB("每月还息，到期还本"),

	/**
	 * 等额本息
	 */
	DEBX("等额本息");

	protected final String chineseName;

	private T6036_F11(String chineseName) {
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
	 * @return {@link T6036_F11}
	 */
	public static final T6036_F11 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6036_F11.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
