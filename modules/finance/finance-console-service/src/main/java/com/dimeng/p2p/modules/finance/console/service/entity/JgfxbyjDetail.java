package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.TradeType;

public class JgfxbyjDetail {

	/**
	 * 时间
	 */
	public Timestamp tradeDateTime;
	/**
	 * 类型名称
	 */
	public TradeType type;
	/**
	 * 收入
	 */
	public BigDecimal organizationCreditAmount = new BigDecimal(0);
	/**
	 * 支出
	 */
	public BigDecimal organizationUsableAmount = new BigDecimal(0);

	/**
	 * 余额
	 */
	public BigDecimal originalReserveAmount = new BigDecimal(0);

	/**
	 * 备注
	 */
	public String remark;
	/**
	 * 用户名
	 */
	public String userName;

}
