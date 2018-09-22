package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户资金统计表
 */
public class T7024 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 可用金额总额
	 */
	public BigDecimal F01 = BigDecimal.ZERO;

	/**
	 * 冻结总金额
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 账户余额总额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 用户总收益
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 借款负债总额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 优选理财资产总额
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 债权资产总额
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 更新时间
	 */
	public Timestamp F08;

}
