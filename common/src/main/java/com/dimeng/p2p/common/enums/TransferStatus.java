package com.dimeng.p2p.common.enums;

public enum TransferStatus {
	/**
	 * 有效
	 */
	YX("有效"),
	/**
	 * 无效
	 */
	WX("无效");
	protected final String name;

	private TransferStatus(String name) {
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
