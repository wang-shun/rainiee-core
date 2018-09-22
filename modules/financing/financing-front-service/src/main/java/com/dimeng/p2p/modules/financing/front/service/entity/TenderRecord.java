package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 投资记录
 * @author gaoshaolong
 *
 */
public class TenderRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4144230775162777727L;
	
	/**
	 * 投资人
	 */
	public String tenderName;
	/**
	 * 投资金额
	 */
	public BigDecimal tenderMoney = new BigDecimal(0);
	/**
	 * 投资时间
	 */
	public Timestamp tenderTime;

}
