package com.dimeng.p2p.common.enums;

/**
 * 是否发布
 * 
 * @author Administrator
 * 
 */
public enum CreditIsstatus {

	/**
	 * 按月还款
	 */
	S("是"), F("否");

	protected final String name;

	private CreditIsstatus(String name) {
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
