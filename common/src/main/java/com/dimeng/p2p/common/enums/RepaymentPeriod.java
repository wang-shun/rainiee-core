package com.dimeng.p2p.common.enums;

/**
 * 还款周期.
 */
public enum RepaymentPeriod {

	/**
	 * 按月还款
	 */
	AYHK("按月还款");

	protected final String name;

	private RepaymentPeriod(String name) {
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
