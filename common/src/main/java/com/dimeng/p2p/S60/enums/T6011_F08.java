package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 实名是否验证
 */
public enum T6011_F08 {

	/**
	 * 驳回
	 */
	BH("驳回"),

	/**
	 * 已验证(通过)
	 */
	YYZ("已验证(通过)"),

	/**
	 * 未验证(审核中)
	 */
	WYZ("未验证(审核中)");

	protected final String chineseName;

	private T6011_F08(String chineseName) {
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
	 * @return {@link T6011_F08}
	 */
	public static final T6011_F08 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6011_F08.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
