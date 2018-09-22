package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 债权持有信息
 * @author gaoshaolong
 *
 */
public class CreditHoldInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8856766543027385691L;
	
	/**
	 * 持有人
	 */
	public String userName;
	/**
	 * 代收本金
	 */
	public BigDecimal dsbj = new BigDecimal(0);
	/**
	 * 投资金额
	 */
	public BigDecimal touzje = new BigDecimal(0);
	/**
	 * 每份金额
	 */
	public BigDecimal mfje = new BigDecimal(0);
	/**
	 * 持有份数
	 */
	public int cyfs;

	
}
