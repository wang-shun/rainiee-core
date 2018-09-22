package com.dimeng.p2p.common.enums;

public enum PaymentStatus {
	WH("未还"), YH("已还"), HKZ("还款中"), YQ("逾期"), YZYQ("严重逾期");
	protected final String name;

	private PaymentStatus(String name) {
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
