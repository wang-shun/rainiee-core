package com.dimeng.p2p.common.enums;

/**
 * 第三方机构的交易类型.
 * 
 */
public enum AgencyTradeType {
	CZ("充值"), CGTX("成功提现"), TXSXF("提现手续费"), CZSXF("充值手续费"), CBFWF("查标服务费");

	protected final String name;

	private AgencyTradeType(String name) {
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
