package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 债权转让-转入表
 */
public class T6040 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 债权转让-转入表自增ID
	 */
	public int F01;

	/**
	 * 债权转让-转出表ID,参考T6039.F01
	 */
	public int F02;

	/**
	 * 转入者ID(投资用户),参考T6010.F01
	 */
	public int F03;

	/**
	 * 转入份数
	 */
	public int F04;

	/**
	 * 成交时间
	 */
	public Timestamp F05;

	/**
	 * 转让手续费(收转出者)
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 格式合同号
	 */
	public String F07;

}
