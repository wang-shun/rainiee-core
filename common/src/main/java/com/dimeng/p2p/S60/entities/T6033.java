package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6033_F05;

/**
 * 充值记录
 */
public class T6033 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户充值记录自增ID
	 */
	public int F01;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F02;

	/**
	 * 创建时间
	 */
	public Timestamp F03;

	/**
	 * 金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 状态,WZF:未支付;ZFCG:支付成功;
	 */
	public T6033_F05 F05;

	/**
	 * 支付公司
	 */
	public String F06;

	/**
	 * 水单号
	 */
	public String F07;

	/**
	 * 支付成功时间
	 */
	public Timestamp F08;

	/**
	 * 充值手续费
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

}
