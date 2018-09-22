package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否成功登录
 */
public enum T6051_F05 {

	/**
	 * 成功
	 */
	CG("成功"), SB("SB");

	protected final String chineseName;

	private T6051_F05(String chineseName) {
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
	 * @return {@link T6051_F05}
	 */
	public static final T6051_F05 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6051_F05.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
