package com.dimeng.p2p.common.enums;

/**
 * 充值状态
 */
public enum ChargeStatus {
	WZF("未支付"), ZFCG("支付成功");

	protected final String name;

	private ChargeStatus(String name) {
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
