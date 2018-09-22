package com.dimeng.p2p.common.enums;

/**
 * 借款意向状态.
 * 
 */
public enum LoanIntentionState {
	/**
	 * 未处理
	 */
	WCL("未处理"),
	/**
	 * 已处理
	 */
	YCL("已处理");

	protected final String name;

	private LoanIntentionState(String name) {
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
