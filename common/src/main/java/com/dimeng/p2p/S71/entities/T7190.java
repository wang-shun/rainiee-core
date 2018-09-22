package com.dimeng.p2p.S71.entities;

import java.sql.Date;

import com.dimeng.framework.service.AbstractEntity;

public class T7190 extends AbstractEntity{

    private static final long serialVersionUID = 1L;
	/**
	 * 时间
	 */
	public Date F01;
	/**
	 * 一级用户投资金额
	 */
	public double F02;
	/**
	 * 一级用户借款金额
	 */
	public double F03;

	/**
	 * 一级用户充值金额
	 */
	public double F04;
	/**
	 * 一级用户提现记录
	 */
	public double F05;
	
	/**
	 * 二级用户投资金额
	 */
	public double F06;
	/**
	 * 二级用户借款金额
	 */
	public double F07;
	/**
	 * 二级用户充值金额
	 */
	public double F08;
	/**
	 * 二级用户提现金额
	 */
	public double F09;
	/**
	 * 业务员姓名
	 */
	public String name;
	/**
	 * 业务员工号
	 */
	public String employNum;
}
