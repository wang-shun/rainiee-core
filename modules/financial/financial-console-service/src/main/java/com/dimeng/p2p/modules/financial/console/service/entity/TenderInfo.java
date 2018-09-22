package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 投资信息
 * @author gongliang
 *
 */
public class TenderInfo {
	
	/**
	 * 投资人
	 */
	public String userName;
	
	/**
	 * 投资金额
	 */
	public BigDecimal tenderMoney = new BigDecimal(0);
	
	/**
	 * 投资时间
	 */
	public Timestamp tenderTime;
}
