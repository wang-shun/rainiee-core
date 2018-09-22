package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 信用等级范围止
 */
public enum T6028_F08 {

	AA("AA"), A("A"), B("B"), C("C"), D("D"), E("E"), HR("HR");

	protected final String chineseName;

	private T6028_F08(String chineseName) {
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
	 * @return {@link T6028_F08}
	 */
	public static final T6028_F08 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6028_F08.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
