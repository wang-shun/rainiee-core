package com.dimeng.p2p.common.enums;

public enum RepayStatus {
	WH("未还"), YH("已还");
	protected final String name;

	private RepayStatus(String name) {
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
