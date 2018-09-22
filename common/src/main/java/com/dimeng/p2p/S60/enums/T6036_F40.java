package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 标的等级
 */
public enum T6036_F40 {

	AA("AA"), A("A"), B("B"), C("C"), D("D"), E("E"), HR("HR");

	protected final String chineseName;

	private T6036_F40(String chineseName) {
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
	 * @return {@link T6036_F40}
	 */
	public static final T6036_F40 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6036_F40.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
