package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否启用
 */
public enum T6028_F10 {

	/**
	 * 停用
	 */
	TY("停用"),

	/**
	 * 启用
	 */
	QY("启用");

	protected final String chineseName;

	private T6028_F10(String chineseName) {
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
	 * @return {@link T6028_F10}
	 */
	public static final T6028_F10 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6028_F10.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
