package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 位置
 */
public enum T7021_F03 {

	/**
	 * 个人中心
	 */
	GRZX("个人中心"),

	/**
	 * 首页
	 */
	SY("首页");

	protected final String chineseName;

	private T7021_F03(String chineseName) {
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
	 * @return {@link T7021_F03}
	 */
	public static final T7021_F03 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7021_F03.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
