package com.dimeng.p2p.repeater.business.entity;

import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;

public class Results extends AbstractEntity{

    private static final long serialVersionUID = 1L;
	/**
	 * 用户名
	 */
	public String name;
	/**
	 * 所属级别
	 */
	public String namelevel;
	/**
	 * 业务员工号
	 */
	public String employNum;
	/**
	 * 注册来源  注册ZC；后台添加HTTJ
	 */
	public T6110_F08 zcly;
	/**
	 * 项目id
	 */
	public String projectID;
	/**
	 * 项目标题
	 */
	public String projectTitle;
	/**
	 * 项目类型
	 */
	public String projectType;
	/**
	 * 投资金额
	 */
	public double investmentAmount; 
	
	/**
	 * 借款金额
	 */
	public double LoanAmount;
	/**
	 * 时间
	 */
	public Date dateTime;
	/**
	 * 标类型
	 * 'jk'为借款
	 */
	public String typejk;
	/**
	 * 用户类型,ZRR:自然人;FZRR:非自然人
	 */
	public T6110_F06 F05;
	/**
	 * 担保方,S:是;F:否;
	 */
	public T6110_F10 F09;
	
	/**
	 * 用户账号
	 */
	public String customName;
	
	public Timestamp showTime;
	
	/**
	 * 所属一级客户用户名
	 */
	public String firstCustomName;
	
	/**
	 * 产品名称
	 */
	public String productName;
	
	/**
	 * 是否按天借款
	 */
	public String isDay;
	
	/**
	 * 借款期限
	 */
	public String term;

	/**
	 * 交易类型
	 */
	public String tradeType;

	/**
	 * 客户姓名
	 */
	public String customRealName;

	/**
	 * 订单ID
	 */
	public int orderId;

	/**
	 * 成交金额
	 */
	public double amount;
}
