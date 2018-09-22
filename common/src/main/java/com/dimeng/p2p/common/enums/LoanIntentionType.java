package com.dimeng.p2p.common.enums;

/**
 * 借款意向状态
 * 
 */
public enum LoanIntentionType {
	GRXY("个人信用标"), GRDYDB("个人担保标");

	protected final String name;

	private LoanIntentionType(String name) {
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
