package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.AutoSetStatus;
import com.dimeng.p2p.common.enums.CreditLevel;

/**
 * 自动投资设置
 * 
 */
public class AutoBidSet {
	/**
	 * 登陆ID
	 */
	public int loginId;
	/**
	 * 用户可用余额
	 */
	public BigDecimal yhkyMoney = new BigDecimal(0);
	/**
	 * 每次投资金额
	 */
	public BigDecimal timeMoney = new BigDecimal(0);
	/**
	 * 利息开始
	 */
	public BigDecimal rateStart = new BigDecimal(0);
	/**
	 * 利息结束
	 */
	public BigDecimal rateEnd = new BigDecimal(0);
	/**
	 * 借款期限开始
	 */
	public int jkqxStart;
	/**
	 * 借款期结束
	 */
	public int jkqxEnd;
	/**
	 * 信用等级开始
	 */
	public CreditLevel levelStart;
	/**
	 * 信用等级结束
	 */
	public CreditLevel levelEnd;
	/**
	 * 账户保留金额
	 */
	public BigDecimal saveMoney = new BigDecimal(0);
	/**
	 * 是否启用
	 */
	public AutoSetStatus autoSetStatus;

}
