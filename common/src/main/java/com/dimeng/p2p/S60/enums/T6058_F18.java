package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 借款类型
 */
public enum T6058_F18 {

	/**
	 * 企业信用标
	 */
	QYXY("企业信用标"),

	/**
	 * 企业抵押担保标
	 */
	QYDYDB("企业抵押担保标");

	protected final String chineseName;

	private T6058_F18(String chineseName) {
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
	 * @return {@link T6058_F18}
	 */
	public static final T6058_F18 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6058_F18.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
