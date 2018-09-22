/*
 * 文 件 名:  BankCardQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年12月18日
 */
package com.dimeng.p2p.modules.account.console.service.query;

/**
 * 银行可信息
 * @author Administrator
 *
 */
public interface BankCardQuery {

	/**
	 * 银行ID
	 * @return
	 */
	public abstract int getBankId();
	/**
	 * 开户行所在地
	 * @return
	 */
	public abstract String getCity();
	/**
	 * 开户行
	 * @return
	 */
	public abstract String getSubbranch();
	/**
	 * 银行卡号
	 * @return
	 */
	public abstract String getBankNumber();
	
	/**
	 * 用户ID
	 * @return
	 */
	public abstract int getAcount();
	
	/**
	 * 银行卡状态
	 * @return
	 */
	public abstract String getStatus();
	
	/**
	 * 开户名
	 * @return
	 */
	public abstract String getUserName();
	
	/**
	 * 开户类型（区分是个人还是企业）
	 * @return
	 */
	public abstract int getType();
}
