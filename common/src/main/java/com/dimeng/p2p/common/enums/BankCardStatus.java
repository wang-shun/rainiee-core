package com.dimeng.p2p.common.enums;

public enum BankCardStatus {
	QY("启用"), TY("停用"), SC("删除");

	protected final String name;

	private BankCardStatus(String name) {
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
