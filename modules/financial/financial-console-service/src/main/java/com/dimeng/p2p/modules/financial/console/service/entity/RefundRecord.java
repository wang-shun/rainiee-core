package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;

/**
 * 还款记录
 * @author gongliang
 *
 */
public class RefundRecord {
	
	/**
	 * 还款详情
	 */
	public RefundInfo[] refundInfos;

	/**
	 * 已还本息
	 */
	public BigDecimal alreadyInterest = new BigDecimal(0);
	
	/**
	 * 待还本息
	 */
	public BigDecimal stayInterest = new BigDecimal(0);

}
