package com.dimeng.p2p.common.enums;

/**
 * 标的类型
 * 
 */
public enum LoanType {
	QYDYDB("企业抵押担保标"), QYXY("企业信用标"), GRDYDB("个人抵押担保标"), GRXY("个人信用标");
	protected final String name;

	private LoanType(String name) {
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
