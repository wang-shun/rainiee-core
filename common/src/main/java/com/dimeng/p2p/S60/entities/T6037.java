package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6037_F07;

/**
 * 投资人列表
 */
public class T6037 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 投资人(原始投资)列表自增ID
	 */
	public int F01;

	/**
	 * 借款标ID,参考T6036.F01
	 */
	public int F02;

	/**
	 * 投资人,参考T6010.F01
	 */
	public int F03;

	/**
	 * 投资金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 投资时间
	 */
	public Timestamp F05;

	/**
	 * 最后更新时间
	 */
	public Timestamp F06;

	/**
	 * 是否自动投资,F:否;S:是;
	 */
	public T6037_F07 F07;

}
