package com.dimeng.p2p.S51.enums;

import com.dimeng.util.StringHelper;

/**
 * 平台垫付类型管理
 */
public enum T5131_F02 {
	/**
	 * 不能垫付
	 */
	N("无"),
	/**
	 * 本金垫付
	 */
	BJ("本金垫付"),
	/**
	 * 本息垫付
	 */
	BX("本息垫付");

	protected final String chineseName;

	private T5131_F02(String chineseName) {
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
	 * @return {@link T5131_F02}
	 */
	public static final T5131_F02 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T5131_F02.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
