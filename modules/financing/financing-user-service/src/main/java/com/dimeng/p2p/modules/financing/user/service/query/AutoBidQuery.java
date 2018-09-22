package com.dimeng.p2p.modules.financing.user.service.query;

import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.CreditLevel;

public interface AutoBidQuery {
	/**
	 * 每次投资金额
	 * 
	 * @return {@link BigDecimal}
	 */
	public abstract BigDecimal getTimeMoney();
	/**
	 * 利息范围开始
	 * 
	 * @return {@link BigDecimal}
	 */
	public abstract BigDecimal getRateStart();
	/**
	 * 利息范围结束
	 * 
	 * @return {@link BigDecimal}
	 */
	public abstract BigDecimal getRateEnd();
	/**
	 * 借款期限开始
	 * 
	 * @return {@link int}
	 */
	public abstract int getJkqxStart();
	/**
	 * 借款期限结束
	 * 
	 * @return {@link int}
	 */
	public abstract int getJkqxEnd();
	/**
	 * 信用等级范围开始
	 * 
	 * @return {@link CreditLevel}
	 */
	public abstract CreditLevel getLevelStart();
	/**
	 * 信用等级范围结束
	 * 
	 * @return {@link CreditLevel}
	 */
	public abstract CreditLevel getLevelEnd();
	/**
	 * 账户保留金额
	 * 
	 * @return {@link BigDecimal}
	 */
	public abstract BigDecimal getSaveMoney();
}
