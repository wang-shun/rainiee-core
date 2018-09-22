package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6034_F07;

/**
 * 提现记录
 */
public class T6034 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 提现记录自增ID
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
	 * 银行卡ID,参考T6024.F01
	 */
	public int F05;

	/**
	 * 费用
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 状态,WSH:未审核;SHTG:审核通过;SHSB:审核失败;TXCG:提现成功;TXSB:提现失败
	 */
	public T6034_F07 F07;

	/**
	 * 审批时间
	 */
	public Timestamp F08;

	/**
	 * 备注
	 */
	public String F09;

	/**
	 * 审批人,参考T7011.F01
	 */
	public int F10;

	/**
	 * 放款人,参考T7011.F01
	 */
	public int F11;

	/**
	 * 放款时间
	 */
	public Timestamp F12;

}
