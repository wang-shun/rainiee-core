package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户投资排行表 -按周
 */
public class T7050 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 账号ID,参考T6110.F01
	 */
	public int F01;
	 
	/**
	 * 登录账号
	 */
	public String F02;
	
	/**
	 * 投资总额
	 */
	public BigDecimal F03;
	
	/**
	 * 周(yyyy+周次)
	 */
	public String F04;
	
	/**
	 * 最后更新时间
	 */
	public Timestamp F05;
	 
}
