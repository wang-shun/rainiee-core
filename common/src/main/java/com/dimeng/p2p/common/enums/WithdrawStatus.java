package com.dimeng.p2p.common.enums;

public enum WithdrawStatus {
	WSH("未审核"), SHTG("审核通过"), SHSB("审核失败"), TXCG("提现成功"), TXSB("提现失败");

	protected final String name;

	private WithdrawStatus(String name) {
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
