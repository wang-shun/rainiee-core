package com.dimeng.p2p.S70.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7032_F04;

/**
 * 催收记录表
 */
public class T7032 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 催收记录表自增ID
	 */
	public int F01;

	/**
	 * 借款标ID,参考T6036.F01
	 */
	public int F02;

	/**
	 * 借款人ID,参考T6010.F01
	 */
	public int F03;

	/**
	 * 催收方式,DH:电话;XC:现场;FL法律
	 */
	public T7032_F04 F04;

	/**
	 * 催收人
	 */
	public String F05;

	/**
	 * 结果概要
	 */
	public String F06;

	/**
	 * 备注
	 */
	public String F07;

	/**
	 * 催收时间
	 */
	public Timestamp F08;

	/**
	 * 操作人,参考T7011.F01
	 */
	public int F09;

	/**
	 * 操作时间
	 */
	public Timestamp F10;

}
