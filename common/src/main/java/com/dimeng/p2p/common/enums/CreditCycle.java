package com.dimeng.p2p.common.enums;

/**
 * 还款周期
 * 
 * @author Administrator
 * 
 */
public enum CreditCycle {

	/**
	 * 按月还款
	 */
	AYHK("按月还款");

	protected final String name;

	private CreditCycle(String name) {
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
