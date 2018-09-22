package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 处理状态
 */
public enum T6058_F12 {

	/**
	 * 已处理
	 */
	YCL("已处理"),

	/**
	 * 未处理
	 */
	WCL("未处理");

	protected final String chineseName;

	private T6058_F12(String chineseName) {
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
	 * @return {@link T6058_F12}
	 */
	public static final T6058_F12 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6058_F12.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
