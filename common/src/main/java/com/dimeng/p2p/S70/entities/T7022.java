package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户充值统计表
 */
public class T7022 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 充值总额
	 */
	public BigDecimal F01 = BigDecimal.ZERO;

	/**
	 * 充值手续费
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 更新时间
	 */
	public Timestamp F03;

}
