package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6041_F12;
import com.dimeng.p2p.S60.enums.T6041_F13;

/**
 * 还款记录表
 */
public class T6041 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 还款记录表自增ID
	 */
	public int F01;

	/**
	 * 借款标ID,参考T6036.F01
	 */
	public int F02;

	/**
	 * 还款人ID,,参考T6010.F01
	 */
	public int F03;

	/**
	 * 期号
	 */
	public int F04;

	/**
	 * 还本金
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 还利息
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 逾期罚息
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 逾期管理费
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 每月交借款管理费
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 应还日期
	 */
	public Date F10;

	/**
	 * 实际还款时间
	 */
	public Timestamp F11;

	/**
	 * 还款状态,WH:未还;YH:已还
	 */
	public T6041_F12 F12;

	/**
	 * 是否提前还款
	 */
	public T6041_F13 F13;

}
