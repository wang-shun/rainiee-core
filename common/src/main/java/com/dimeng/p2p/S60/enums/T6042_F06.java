package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 投资范围
 */
public enum T6042_F06 {

	/**
	 * 实地认证+机构担保
	 */
	SDRZJJGDB("实地认证+机构担保"),

	/**
	 * 实地认证
	 */
	SDRZ("实地认证"),

	/**
	 * 机构担保
	 */
	JGDB("机构担保");

	protected final String chineseName;

	private T6042_F06(String chineseName) {
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
	 * @return {@link T6042_F06}
	 */
	public static final T6042_F06 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6042_F06.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
