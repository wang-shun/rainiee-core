package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 状态
 */
public enum T6034_F07 {

	/**
	 * 提现失败
	 */
	TXSB("提现失败"),

	/**
	 * 提现成功
	 */
	TXCG("提现成功"),

	/**
	 * 审核失败
	 */
	SHSB("审核失败"),

	/**
	 * 审核通过
	 */
	SHTG("审核通过"),

	/**
	 * 未审核
	 */
	WSH("未审核");

	protected final String chineseName;

	private T6034_F07(String chineseName) {
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
	 * @return {@link T6034_F07}
	 */
	public static final T6034_F07 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6034_F07.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
