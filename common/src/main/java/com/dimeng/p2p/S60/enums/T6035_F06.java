package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 状态
 */
public enum T6035_F06 {

	/**
	 * 已读
	 */
	YD("已读"),

	/**
	 * 未读
	 */
	WD("未读"),

	/**
	 * 删除
	 */
	SC("删除");

	protected final String chineseName;

	private T6035_F06(String chineseName) {
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
	 * @return {@link T6035_F06}
	 */
	public static final T6035_F06 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6035_F06.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
