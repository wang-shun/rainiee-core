package com.dimeng.p2p.modules.financial.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 优选理财统计
 *
 */
public class YxlcCount  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8444343751160693853L;

	/**
	 * 累计总金额
	 */
	public BigDecimal totleMoney = new BigDecimal(0);
	
	/**
	 * 资金利用率
	 */
	public double moneyUse;
	
	/**
	 * 为用户累计赚取
	 */
	public BigDecimal userEarnMoney = new BigDecimal(0);
	/**
	 * 加入总人次
	 */
	public long joinCount;
	
	

}
