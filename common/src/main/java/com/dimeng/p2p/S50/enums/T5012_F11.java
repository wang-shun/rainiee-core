package com.dimeng.p2p.S50.enums;

import com.dimeng.util.StringHelper;

/**
 * 客服状态
 */
public enum T5012_F11 {

	/**
	 * 启用
	 */
	QY("启用"),

	/**
	 * 停用
	 */
	TY("停用");

	protected final String chineseName;

	private T5012_F11(String chineseName) {
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
	 * @return {@link T5012_F11}
	 */
	public static final T5012_F11 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T5012_F11.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
