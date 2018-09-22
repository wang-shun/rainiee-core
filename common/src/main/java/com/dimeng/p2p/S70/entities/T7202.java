package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 法人用户资金统计表
 */
public class T7202 extends AbstractEntity {

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
	 * 借款负债总额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 更新时间
	 */
	public Timestamp F05;

}
