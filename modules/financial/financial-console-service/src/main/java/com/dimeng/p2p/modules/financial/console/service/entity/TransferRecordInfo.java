package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 转让记录
 * @author gongliang
 *
 */
public class TransferRecordInfo {

	/**
	 * 债权买入者
	 */
	public String transferCome;
	
	/**
	 * 债权买出者
	 */
	public String transferOut;
	
	/**
	 * 交易金额
	 */
	public BigDecimal dealMoney = new BigDecimal(0);
	
	/**
	 * 交易时间
	 */
	public Timestamp dealTime;
}
