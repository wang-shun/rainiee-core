package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6038_F07;

/**
 * 用户债权表
 */
public class T6038 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 债权记录自增ID
	 */
	public int F01;

	/**
	 * 借款标ID,参考T6036.F01
	 */
	public int F02;

	/**
	 * 持有人ID,参考T6010.F01
	 */
	public int F03;

	/**
	 * 持有金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 持有时间
	 */
	public Timestamp F05;

	/**
	 * 最后更新时间
	 */
	public Timestamp F06;

	/**
	 * 是否转出,F:否;S:是
	 */
	public T6038_F07 F07;

	/**
	 * 持有份数
	 */
	public int F08;

	/**
	 * 债权违约金
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 投资金额
	 */
	public BigDecimal F10 = BigDecimal.ZERO;

}
