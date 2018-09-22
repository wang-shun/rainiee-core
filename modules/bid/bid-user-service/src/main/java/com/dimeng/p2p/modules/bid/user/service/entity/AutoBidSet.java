package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.AutoSetStatus;

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
	public int levelStart;
	/**
	 * 信用等级结束
	 */
	public int levelEnd;
	/**
	 * 账户保留金额
	 */
	public BigDecimal saveMoney = new BigDecimal(0);
	/**
	 * 是否启用
	 */
	public AutoSetStatus autoSetStatus;
	
	/**
	 * 设置时间
	 * <功能详细描述>
	 * @return
	 */
	public Timestamp setTime;
	
	public int id;

	/**
	 * 投资金额是否是全部
	 */
	public int all;

}
