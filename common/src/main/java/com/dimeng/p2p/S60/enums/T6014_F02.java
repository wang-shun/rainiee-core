package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 职业状态
 */
public enum T6014_F02 {

	/**
	 * 私营企业主
	 */
	SYQYZ("私营企业主");

	protected final String chineseName;

	private T6014_F02(String chineseName) {
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
	 * @return {@link T6014_F02}
	 */
	public static final T6014_F02 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6014_F02.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
