package com.dimeng.p2p.modules.account.console.service.entity;

public enum FundXYAccountType {
	/**
	 * 个人
	 */
	GR("个人"),
	/**
	 * 企业
	 */
	QY("企业"),

	/**
	 * 机构
	 */
	JG("机构");
	
	protected final String name;

	private FundXYAccountType(String name) {
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
