package com.dimeng.p2p.S61.enums;

import com.dimeng.util.StringHelper;

/**
 * 手机认证状态
 */
public enum T6119_F03 {

	/**
	 * 通过
	 */
	TG("通过"),

	/**
	 * 不通过
	 */
	BTG("不通过");

	protected final String chineseName;

	private T6119_F03(String chineseName) {
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
	 * @return {@link T6119_F03}
	 */
	public static final T6119_F03 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6119_F03.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
