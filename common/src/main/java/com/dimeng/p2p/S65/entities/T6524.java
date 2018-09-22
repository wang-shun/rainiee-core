/*
 * 文 件 名:  T6524.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月13日
 */
package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 加息券放款订单 <功能详细描述>
 * 
 * @author xiaoqi
 * @version [v3.1.2, 2015年10月13日]
 */
public class T6524 extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID,参考T6501.F01
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
	 * 投资记录ID,参考T6250.F01
	 */
	public int F04;

	/**
	 * 加息券利率
	 */
	public BigDecimal F05;

	/**
	 * 使用加息券投资金额
	 */
	public BigDecimal F06;

}
