package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 还款周期
 */
public enum T6036_F10 {

	/**
	 * 按月还款
	 */
	AYHK("按月还款");

	protected final String chineseName;

	private T6036_F10(String chineseName) {
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
	 * @return {@link T6036_F10}
	 */
	public static final T6036_F10 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6036_F10.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
