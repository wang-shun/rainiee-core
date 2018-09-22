package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否成功登录
 */
public enum T7012_F05 {

	/**
	 * 成功
	 */
	CG("成功"),

	/**
	 * 失败
	 */
	SB("失败");

	protected final String chineseName;

	private T7012_F05(String chineseName) {
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
	 * @return {@link T7012_F05}
	 */
	public static final T7012_F05 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7012_F05.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
