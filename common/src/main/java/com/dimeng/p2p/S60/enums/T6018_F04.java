package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 认证状态
 */
public enum T6018_F04 {

	/**
	 * 未验证
	 */
	WYZ("未验证"),

	/**
	 * 未通过
	 */
	WTG("未通过"),

	/**
	 * 已验证
	 */
	YYZ("已验证"),

	/**
	 * 待审核
	 */
	DSH("待审核");

	protected final String chineseName;

	private T6018_F04(String chineseName) {
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
	 * @return {@link T6018_F04}
	 */
	public static final T6018_F04 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6018_F04.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
