package com.dimeng.p2p.modules.bid.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 线上债权转让统计
 */
public class Zqzrtj  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8444343751160693853L;

	/**
	 * 累计总金额
	 */
	public BigDecimal totleMoney = new BigDecimal(0);
	
	/**
	 * 累计总笔数
	 */
	public long totleCount;
	
	/**
	 * 为用户累计赚取
	 */
	public BigDecimal userEarnMoney = new BigDecimal(0);
	

}
