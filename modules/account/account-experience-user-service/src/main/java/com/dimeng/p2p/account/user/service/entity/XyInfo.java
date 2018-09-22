package com.dimeng.p2p.account.user.service.entity;

import java.math.BigDecimal;

/**
 * 信用信息
 *
 */
public class XyInfo {
	/**
	 * 信用积分
	 */
	public int score;
	/**
	 * 信用等级
	 */
	public String xydj;
	/**
	 * 信用额度
	 */
	public BigDecimal xyed = new BigDecimal(0);
}
