package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 还款状态
 */
public enum T6041_F12 {

	/**
	 * 未还
	 */
	WH("未还"),

	/**
	 * 已还
	 */
	YH("已还");

	protected final String chineseName;

	private T6041_F12(String chineseName) {
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
	 * @return {@link T6041_F12}
	 */
	public static final T6041_F12 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6041_F12.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
