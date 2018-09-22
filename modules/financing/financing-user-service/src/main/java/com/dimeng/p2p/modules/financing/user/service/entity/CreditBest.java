package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.EarningsWay;
import com.dimeng.p2p.common.enums.PlanState;

public class CreditBest {

	/**
	 * 还需金额
	 */
	public BigDecimal remainAmount = new BigDecimal(0);
	/**
	 * 申请时间
	 */
	public Timestamp sqTime;
	/**
	 * 投资上限
	 */
	public BigDecimal maxBidMoney;
	/**
	 * 投资下限
	 */
	public BigDecimal minBidMoney;
	/**
	 * 状态
	 */
	public PlanState planState;
	/**
	 * 加入费率
	 */
	public BigDecimal jrfl;
	/**
	 * 服务费率
	 */
	public BigDecimal fwfl;
	/**
	 * 收益费率
	 */
	public BigDecimal rate;
	/**
	 * 锁定期限
	 */
	public int lockQx;;
	
	/**
	 * 收益方式(MYHXDQHB:每月还息，到期还本;DQBXYCXZF:到期本息一次性支付;DEBXMYHF:等额本息每月返还)
	 */
	public EarningsWay earningsWay;
	/**
	 * 实际投资金额
	 */
	public BigDecimal planMoney;
}
