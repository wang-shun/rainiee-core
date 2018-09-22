package com.dimeng.p2p.common.enums;

/**
 * 交易类型明细
 * 
 */
public enum TradeType {

	DF("垫付"), DFHF("垫付返还"), JKCJFWF("借款成交服务费"), SDZJBZJ("手动增加保证金"), SDKCBZJ(
			"手动扣除保证金");

	protected final String name;

	private TradeType(String name) {
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
