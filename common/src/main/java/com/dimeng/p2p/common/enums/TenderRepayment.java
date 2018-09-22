package com.dimeng.p2p.common.enums;

public enum TenderRepayment {
	/**
	 * 等额本息
	 */
	DEBX("等额本息");
	protected final String name;

	private TenderRepayment(String name) {
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
