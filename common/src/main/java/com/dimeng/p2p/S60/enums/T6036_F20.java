package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 标的状态
 */
public enum T6036_F20 {

	/**
	 * 流标
	 */
	LB("流标"),

	/**
	 * 已垫付
	 */
	YDF("已垫付"),

	/**
	 * 已结清
	 */
	YJQ("已结清"),

	/**
	 * 待审核
	 */
	DSH("待审核"),

	/**
	 * 申请中
	 */
	SQZ("申请中"),

	/**
	 * 已放款(还款中)
	 */
	YFK("已放款(还款中)"),

	/**
	 * 已满标
	 */
	YMB("已满标"),

	/**
	 * 投资中
	 */
	TBZ("投资中");

	protected final String chineseName;

	private T6036_F20(String chineseName) {
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
	 * @return {@link T6036_F20}
	 */
	public static final T6036_F20 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6036_F20.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
