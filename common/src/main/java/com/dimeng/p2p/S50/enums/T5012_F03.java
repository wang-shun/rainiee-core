package com.dimeng.p2p.S50.enums;

import com.dimeng.util.StringHelper;

/**
 * 客服类型
 */
public enum T5012_F03 {

	/**
	 * 邮箱
	 */
	YX("邮箱"),

	/**
	 * 电话号码
	 */
	DH("电话号码"),

	/**
	 * QQ
	 */
	QQ("个人QQ"),

	/**
	 * 企业QQ
	 */
	QQ_QY("企业QQ");

	protected final String chineseName;

	private T5012_F03(String chineseName) {
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
	 * @return {@link T5012_F03}
	 */
	public static final T5012_F03 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T5012_F03.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
