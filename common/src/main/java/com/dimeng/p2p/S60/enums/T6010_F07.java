package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 注册来源
 */
public enum T6010_F07 {

	/**
	 * 后台
	 */
	HT("后台"),

	/**
	 * 注册
	 */
	ZC("注册");

	protected final String chineseName;

	private T6010_F07(String chineseName) {
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
	 * @return {@link T6010_F07}
	 */
	public static final T6010_F07 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6010_F07.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
