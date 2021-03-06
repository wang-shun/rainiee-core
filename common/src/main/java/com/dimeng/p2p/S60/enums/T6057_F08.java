package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 还款状态
 */
public enum T6057_F08 {

	/**
	 * 未还
	 */
	WH("未还"),

	/**
	 * 已还
	 */
	YH("已还");

	protected final String chineseName;

	private T6057_F08(String chineseName) {
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
	 * @return {@link T6057_F08}
	 */
	public static final T6057_F08 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6057_F08.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
