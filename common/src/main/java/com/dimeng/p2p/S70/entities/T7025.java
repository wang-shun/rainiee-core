package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 平台资金账户
 */
public class T7025 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 账户余额
	 */
	public BigDecimal F01 = BigDecimal.ZERO;

	/**
	 * 总支出
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 总收入
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 盈亏
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 最后更新时间
	 */
	public Timestamp F05;

	/**
	 * 提现总额
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 充值总额
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

}
