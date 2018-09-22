package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 借款类型
 */
public enum T6050_F05 {

	/**
	 * 个人抵押担保）
	 */
	GRDYDB("个人抵押担保）"),

	/**
	 * 个人信用
	 */
	GRXY("个人信用"), JGDB("JGDB"),

	/**
	 * 实地认证
	 */
	SDRZ("实地认证");

	protected final String chineseName;

	private T6050_F05(String chineseName) {
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
	 * @return {@link T6050_F05}
	 */
	public static final T6050_F05 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6050_F05.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
