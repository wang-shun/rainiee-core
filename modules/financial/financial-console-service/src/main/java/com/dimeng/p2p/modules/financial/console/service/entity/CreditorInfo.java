package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;

/**
 * 债权信息
 * @author gongliang
 *
 */
public class CreditorInfo {
	/**
	 * 债权人
	 */
	public String userName;
	
	/**
	 * 持有金额
	 */
	public BigDecimal tenderMoney = new BigDecimal(0);
	
	/**
	 * 持有份数
	 */
	public int tenderTime;
}
