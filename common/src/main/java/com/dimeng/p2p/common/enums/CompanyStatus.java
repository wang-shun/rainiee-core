package com.dimeng.p2p.common.enums;

/**
 * 职业状态
 */
public enum CompanyStatus {

	/**
	 * 私营企业主
	 */
	SYQYZ("私营企业主");

	protected final String name;

	private CompanyStatus(String name) {
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
