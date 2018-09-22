package com.dimeng.p2p.common.enums;

/**
 * 转让状态.
 * 
 */
public enum TransferState {
	/**
	 * 有效
	 */
	YX("有效"),
	/**
	 * 无效
	 */
	WX("无效");

	protected final String name;

	private TransferState(String name) {
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
