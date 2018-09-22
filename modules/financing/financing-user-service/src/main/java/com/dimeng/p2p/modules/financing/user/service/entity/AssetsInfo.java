package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;

/**
 * 债权信息实体类
 * 
 */
public class AssetsInfo {
	/**
	 * 债权已赚金额
	 */
	public BigDecimal makeMoney = new BigDecimal(0);
	/**
	 * 利息收益
	 */
	public BigDecimal accMakeMoney = new BigDecimal(0);
	/**
	 * 债权转让盈亏
	 */
	public BigDecimal sellMakeMoney = new BigDecimal(0);
	/**
	 * 债权账户资产
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 回收中的债权数量
	 */
	public int assetsNum;
}
