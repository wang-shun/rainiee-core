package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.sql.Date;

import com.dimeng.p2p.common.enums.CreditStatus;

/**
 * 已结清的债权实体类
 *
 */
public class SettleAssests {
	/**
	 * 借款标ID
	 */
	public int jkbId;
	/**
	 * 债权ID
	 */
	public int assestsID;
	/**
	 * 投资金额
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 年化利率
	 */
	public double rate;
	/**
	 * 回收金额
	 */
	public BigDecimal recMoney = new BigDecimal(0);
	/**
	 * 已赚金额
	 */
	public BigDecimal makeMoney = new BigDecimal(0);
	/**
	 * 结清日期
	 */
	public Date settleDay;
	/**
	 * 结清方式
	 */
	public CreditStatus settleMethod;
}
