package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 线上债权转让信息
 *
 */
public class SellFinacingInfo {
	/**
	 * 线上债权转让盈亏
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 成功转入金额
	 */
	public BigDecimal inMoney = new BigDecimal(0);
	/**
	 * 成功转出金额
	 */
	public BigDecimal outMoney = new BigDecimal(0);
	/**
	 * 债权转入盈亏
	 */
	public BigDecimal inAssetsMoney = new BigDecimal(0);
	/**
	 * 债权转出盈亏
	 */
	public BigDecimal outAssetsMoney = new BigDecimal(0);
	/**
	 * 已转入债权笔数
	 */
	public int inNum;
	/**
	 * 已转出债权笔数
	 */
	public int outNum;
}
