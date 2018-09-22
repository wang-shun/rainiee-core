package com.dimeng.p2p.common.enums;

public enum FundAccountType {
	/**
	 * 个人
	 */
	GR("个人"),
	/**
	 * 企业
	 */
	QY("企业"),

	/**
	 * 平台
	 */
	PT("平台"),
	
	/**
	 * 机构
	 */
	JG("担保机构");
	
	protected final String name;

	private FundAccountType(String name) {
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
