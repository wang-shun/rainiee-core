package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7047_F05;

/**
 * 活动-参与人列表
 */
public class T7047 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 活动-参与人列表自增ID
	 */
	public int F01;

	/**
	 * 活动ID,参考T7046.F01
	 */
	public int F02;

	/**
	 * 参与人ID,参考T6010.F01
	 */
	public int F03;

	/**
	 * 奖励金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 是否受益,S:是;F:否
	 */
	public T7047_F05 F05;

	/**
	 * 参与时间
	 */
	public Timestamp F06;

}
