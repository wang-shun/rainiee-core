package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;

/**
 * 投资记录
 * @author gongliang
 *
 */
public class TenderRecord {
	
	/**
	 * 投资信息
	 */
	public TenderInfo[] tenderRecords;
	
	/**
	 * 加入总人数
	 */
	public int totalNumber;
	
	/**
	 * 投资总金额
	 */
	public BigDecimal totalMoney = new BigDecimal(0);
}
