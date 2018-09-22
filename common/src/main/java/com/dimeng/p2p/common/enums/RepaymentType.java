package com.dimeng.p2p.common.enums;

/**
 * 还款方式.
 */
public enum RepaymentType {

	/**
	 * 等额本息
	 */
	DEBX("等额本息");

	protected final String name;

	private RepaymentType(String name) {
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
