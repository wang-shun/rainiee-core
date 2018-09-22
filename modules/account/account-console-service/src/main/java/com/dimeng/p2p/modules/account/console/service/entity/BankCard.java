/*
 * 文 件 名:  BankCard.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年12月18日
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.io.Serializable;

/**
 * 银行卡信息 
 * @author  xiaoqi
 * @version  [版本号, 2015年12月18日]
 */
public class BankCard implements Serializable{
	
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	public int id;
	/**
	 * 用户ID
	 */
	public int acount;
	/**
	 * 银行ID
	 */
	public int BankID;
	/**
	 * 银行名称
	 */
	public String Bankname;
	/**
	 * 开户行地址
	 */
	public String City;
	/**
	 * 开户行名称
	 */
	public String BankKhhName;
	
	/**
	 * 银行卡号
	 */
	public String BankNumber;
	/**
	 * 银行卡状态
	 */
	public String status;
	
	/**
	 * 开户名
	 */
	public String userName;

	/**
	 * 开户类型（区分是个人还是企业）
	 */
	public int type;
	
}
