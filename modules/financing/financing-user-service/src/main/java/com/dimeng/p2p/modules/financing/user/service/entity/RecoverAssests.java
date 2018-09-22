package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.CreditStatus;

/**
 * 回收中的债权
 *
 */
public class RecoverAssests {
	/**
	 * 借款标Id
	 */
	public int jkbId;
	/**
	 * 债权ID
	 */
	public int assestsID;
	/**
	 * 原始投资金额
	 */
	public BigDecimal originalMoney = new BigDecimal(0);
	/**
	 * 年化利率
	 */
	public double rate;
	/**
	 * 待收本息
	 */
	public BigDecimal recoverAcc = new BigDecimal(0);
	/**
	 * 月收本息
	 */
	public BigDecimal recoverMonthAcc = new BigDecimal(0);
	/**
	 * 剩余期数
	 */
	public int assestsNum;
	/**
	 * 借款期限
	 */
	public int creditNum;
	/**
	 * 下个还款日
	 */
	public Timestamp lastDay;
	/**
	 * 状态
	 */
	public CreditStatus status;
	/**
	 * 是否可以转让
	 */
	public boolean isTransfer;
	/**
	 * 持有份数
	 */
	public int cyfs;
	/**
	 * 债权价值
	 */
	public BigDecimal zqjz = new BigDecimal(0);
	/**
	 * 持有金额
	 */
	public BigDecimal cyje = new BigDecimal(0);
	

}
