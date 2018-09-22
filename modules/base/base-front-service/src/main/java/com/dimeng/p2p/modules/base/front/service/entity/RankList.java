package com.dimeng.p2p.modules.base.front.service.entity;

import java.math.BigDecimal;

/**
 * 理财排行榜 
 *
 */
public class RankList {
	/**
	 * 用户名
	 */
	public String userName;
	/**
	 * 理财总金额
	 */
	public BigDecimal money = new BigDecimal(0);
}
