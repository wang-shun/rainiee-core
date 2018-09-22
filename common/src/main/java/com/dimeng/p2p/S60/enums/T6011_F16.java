package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 性别
 */
public enum T6011_F16 {

	/**
	 * 其他
	 */
	QT("其他"),

	/**
	 * 女
	 */
	F("女"),

	/**
	 * 男
	 */
	M("男");

	protected final String chineseName;

	private T6011_F16(String chineseName) {
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
	 * @return {@link T6011_F16}
	 */
	public static final T6011_F16 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6011_F16.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
