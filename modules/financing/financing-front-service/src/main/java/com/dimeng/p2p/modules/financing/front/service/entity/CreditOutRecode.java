package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 转让记录
 * @author gaoshaolong
 *
 */
public class CreditOutRecode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5514793456669670810L;
	
	/**
	 * 债权购买者
	 */
	public String creditBuy;
	/**
	 * 债权卖出者
	 */
	public String creditSell;
	/**
	 * 交易金额
	 */
	public BigDecimal sellMoney = new BigDecimal(0);
	/**
	 * 交易时间
	 */
	public Timestamp sellTime ;
	/**
	 * 交易份数
	 */
	public int jyfs;
	/**
	 * 交易单价
	 */
	public BigDecimal jydj;
	
	

}
