package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户提现统计表
 */
public class T7023 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 提现总额
	 */
	public BigDecimal F01 = BigDecimal.ZERO;

	/**
	 * 提现手续费
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 更新时间
	 */
	public Timestamp F03;

}
