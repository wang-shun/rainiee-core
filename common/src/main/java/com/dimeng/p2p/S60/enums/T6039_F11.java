package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 转让状态
 */
public enum T6039_F11 {

	/**
	 * 有效
	 */
	YX("有效"),

	/**
	 * 无效
	 */
	WX("无效");

	protected final String chineseName;

	private T6039_F11(String chineseName) {
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
	 * @return {@link T6039_F11}
	 */
	public static final T6039_F11 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6039_F11.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
