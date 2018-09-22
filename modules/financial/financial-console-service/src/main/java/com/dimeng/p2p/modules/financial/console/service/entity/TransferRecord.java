package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;

/**
 * 转让记录
 * @author gongliang
 *
 */
public class TransferRecord {
	
	/**
	 * 转让详情
	 */
	public TransferRecordInfo[] refundInfos;

	/**
	 * 交易总额
	 */
	public BigDecimal totalDealMoney = new BigDecimal(0);

}
