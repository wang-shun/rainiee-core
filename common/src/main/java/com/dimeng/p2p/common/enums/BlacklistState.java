package com.dimeng.p2p.common.enums;

/**
 * 拉黑状态.
 * 
 */
public enum BlacklistState {
	/**
	 * 拉黑
	 */
	LH("拉黑"),
	/**
	 * 取消拉黑
	 */
	QXLH("取消拉黑");

	protected final String name;

	private BlacklistState(String name) {
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
