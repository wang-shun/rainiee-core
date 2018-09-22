package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6113_F04;
import com.dimeng.p2p.S60.enums.T6113_F07;

/**
 * 优惠券-用户关系表
 */
public class T6113 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public int F01;

	/**
	 * 活动id，参考T6110.F01
	 */
	public int F02;

	/**
	 * 用户id，参考F6010.F01
	 */
	public int F03;

	/**
	 * WSY:未使用;YSY:已使用;YGQ:已过期;
	 */
	public T6113_F04 F04;

	/**
	 * 领取时间
	 */
	public Timestamp F05;

	/**
	 * 金额
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * CZ:充值;TZ:投资;
	 */
	public T6113_F07 F07;

	/**
	 * 投资记录id或充值记录id
	 */
	public int F08;

	/**
	 * 使用时间
	 */
	public Timestamp F09;

}
