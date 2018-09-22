/*
 * 文 件 名:  T6523.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月14日
 */
package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 红包投资订单
 * 
 * @version [v3.1.2, 2015年10月14日]
 */
public class T6527 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	public int F01;

	/**
	 * 投资用户ID,参考T6110.F01
	 */
	public int F02;

	/**
	 * 标ID,参考T6230.F01
	 */
	public int F03;

	/**
	 * 红包金额
	 */
	public BigDecimal F04;

	/**
	 * 红包投资记录ID,参考t6292.F01,投资成功时记录
	 */
	public int F05;

	/**
	 * 投资订单ID，参考T6504.F01
	 */
	public int F06;
}
