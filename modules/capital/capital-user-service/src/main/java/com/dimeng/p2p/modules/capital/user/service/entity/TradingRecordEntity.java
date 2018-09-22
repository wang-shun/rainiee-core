package com.dimeng.p2p.modules.capital.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.TradingType;

public class TradingRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 记录id
	 */
	public int id;
	/**
	 * 交易时间
	 */
	public Timestamp tradeTime;
	/**
	 * 交易类型
	 */
	public TradingType type;
	/**
	 * 收入
	 */
	public BigDecimal amountIn = new BigDecimal(0);
	/**
	 * 支出
	 */
	public BigDecimal amountOut = new BigDecimal(0);
	/**
	 * 结余
	 */
	public BigDecimal balance = new BigDecimal(0);
	/**
	 * 备注
	 */
	public String remark;
}
