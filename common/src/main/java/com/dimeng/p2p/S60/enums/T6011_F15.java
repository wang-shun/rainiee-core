package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 兴趣类型
 */
public enum T6011_F15 {

	/**
	 * 借款
	 */
	JK("借款"),

	/**
	 * 理财
	 */
	LC("理财");

	protected final String chineseName;

	private T6011_F15(String chineseName) {
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
	 * @return {@link T6011_F15}
	 */
	public static final T6011_F15 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6011_F15.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
