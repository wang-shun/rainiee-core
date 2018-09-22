package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6056_F10;
import com.dimeng.p2p.S60.enums.T6056_F14;

/**
 * 债权人收款记录表
 */
public class T6056 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 借款标ID,参考T6036.F01
	 */
	public int F02;

	/**
	 * 债权人ID,对应T6010.F01
	 */
	public int F03;

	/**
	 * 期号
	 */
	public int F04;

	/**
	 * 待收本金
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 待收利息
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 待收逾期罚息
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 应收日期
	 */
	public Date F08;

	/**
	 * 实际收款日期
	 */
	public Timestamp F09;

	/**
	 * 收款状态
	 */
	public T6056_F10 F10;

	/**
	 * 债权持有份数
	 */
	public int F11;

	/**
	 * 每份应收本金
	 */
	public BigDecimal F12 = BigDecimal.ZERO;

	/**
	 * 每份应收利息
	 */
	public BigDecimal F13 = BigDecimal.ZERO;

	/**
	 * 是否提前收款
	 */
	public T6056_F14 F14;

}
