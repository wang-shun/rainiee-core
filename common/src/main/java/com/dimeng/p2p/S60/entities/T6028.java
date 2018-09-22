package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6028_F07;
import com.dimeng.p2p.S60.enums.T6028_F08;
import com.dimeng.p2p.S60.enums.T6028_F10;

/**
 * 自动投资设置
 */
public class T6028 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号ID,参考T6010.F10
	 */
	public int F01;

	/**
	 * 每次投资金额
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 利息范围开始
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 利息范围截止
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 借款期限开始(月数)
	 */
	public int F05;

	/**
	 * 借款期限截止
	 */
	public int F06;

	/**
	 * 信用等级范围起
	 */
	public T6028_F07 F07;

	/**
	 * 信用等级范围止
	 */
	public T6028_F08 F08;

	/**
	 * 账户保留金额
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 是否启用,QY:启用;TY:停用;
	 */
	public T6028_F10 F10;

	/**
	 * 设置时间
	 */
	public Timestamp F11;

}
