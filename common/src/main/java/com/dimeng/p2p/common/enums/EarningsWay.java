package com.dimeng.p2p.common.enums;

/**
 * 收益方式.
 * 
 */
public enum EarningsWay {
	/**
	 * 每月还息，到期还本
	 */
	MYHXDQHB("每月还息，到期还本"),
	/**
	 * 到期本息一次性支付
	 */
	DQBXYCXZF("到期本息一次性支付"),
	/**
	 * 等额本息每月返还
	 */
	DEBXMYHF("等额本息每月返还");

	protected final String name;

	private EarningsWay(String name) {
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
