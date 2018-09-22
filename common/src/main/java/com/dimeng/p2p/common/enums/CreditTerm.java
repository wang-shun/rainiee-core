package com.dimeng.p2p.common.enums;

/**
 * 借款期限.
 */
public enum CreditTerm {
	/**
	 * 3个月以下
	 */
	SGYYX("3个月以下"),
	/**
	 * 3-6个月
	 */
	SDLGY("3-6个月"),
	/**
	 * 6-9个月
	 */
	LDJGY("6-9个月"),
	/**
	 * 9-12个月
	 */
	JDSEGY("9-12个月"),
	/**
	 * 12个月以上
	 */
	SEGYYS("12个月以上");

	protected final String name;

	private CreditTerm(String name) {
		this.name = name;
	}

	/**
	 * 获取中文名称.
	 * 
	 * @return {@link String}
	 */
	public String getName() {
		return name;

	}
}
