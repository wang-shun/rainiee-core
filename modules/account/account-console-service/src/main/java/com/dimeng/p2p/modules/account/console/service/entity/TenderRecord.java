package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;

/**
 * 投资记录
 * @author gongliang
 *
 */
public class TenderRecord {
	/**
	 * 债权ID
	 */
    public String tenderRecordId;
	
	/**
	 * 债权标题
	 */
	public String tenderRecordTitle;
	
	/**
	 * 投资金额
	 */
	public BigDecimal tenderMoney = new BigDecimal(0);
	
	/**
	 * 待收本息
	 */
	public BigDecimal takeCoupon = new BigDecimal(0);
	
	/**
	 * 年化利率
	 */
	public double yearRate;
	
	/**
	 * 期限
	 */
	public int deadline;
	
	/**
	 * 投资时间
	 */
	public Timestamp tenderTime;
	
	/**
	 * 标的状态
	 */
	public T6230_F20 tenderRecordState;
	
	/**
	 * 是否按天借款
	 */
	public T6231_F21 dayBorrowFlg;
}
