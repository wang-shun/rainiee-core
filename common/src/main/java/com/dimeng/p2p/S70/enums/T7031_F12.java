package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 合同状态
 */
public enum T7031_F12 {

	WX("WX"),

	/**
	 * 有效
	 */
	YX("有效");

	protected final String chineseName;

	private T7031_F12(String chineseName) {
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
	 * @return {@link T7031_F12}
	 */
	public static final T7031_F12 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7031_F12.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
