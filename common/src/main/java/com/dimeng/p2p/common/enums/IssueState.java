package com.dimeng.p2p.common.enums;

/**
 * 发布状态.
 * 
 */
public enum IssueState {
	/**
	 * 已发布
	 */
	S("已发布"),
	/**
	 * 未发布
	 */
	F("未发布");

	protected final String name;

	private IssueState(String name) {
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
