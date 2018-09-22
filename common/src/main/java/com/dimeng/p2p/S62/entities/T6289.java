/*
 * 文 件 名:  T6289.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月13日
 */
package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6289_F09;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author xiaoqi
 * @version [v3.1.2, 2015年10月13日]
 */
public class T6289 extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 标ID,参考T6230.F01
	 */
	public int F02;

	/**
	 * 付款平台ID,参考T6110.F01
	 */
	public int F03;

	/**
	 * 收款用户ID,参考T6110.F01
	 */
	public int F04;

	/**
	 * 交易类型ID,参考T5122.F01
	 */
	public int F05;

	/**
	 * 期号
	 */
	public int F06;

	/**
	 * 加息券返还利息
	 */
	public BigDecimal F07;

	/**
	 * 返还日期
	 */
	public Date F08;

	/**
	 * 状态,WFH:未返还;YFH:已返还;YSX:已失效;
	 */
	public T6289_F09 F09;

	/**
	 * 实际返还时间
	 */
	public Timestamp F10;

	/**
	 * 剩余加息券返还利息
	 */
	public BigDecimal F11;

	/**
	 * 加息券ID,参考T6342.F01
	 */
	public int F12;

	/**
	 * 债权ID,参考T6251.F01
	 */
	public int F13;
}
