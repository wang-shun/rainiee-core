package com.dimeng.p2p.modules.capital.user.service;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.modules.capital.user.service.entity.BankCard;

/**
 * 提现
 */
public abstract interface WithdrawManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 获取用户添加的银行卡
	 * </dl>
	 * <dl>
	 * <ol>
	 * 查询表T6024，注意银行卡应为启用状态
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return 用户启用状态银行卡
	 * @throws SQLException
	 */
	public abstract BankCard[] bankCards() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 获取可用资金
	 * </dl>
	 * <dl>
	 * <ol>
	 * T6023.F05字段
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return 可用资金
	 * @throws SQLException
	 */
	public abstract BigDecimal availableFunds() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 提现
	 * </dl>
	 * <dl>
	 * <ol>
	 * <li>判断参数funds>=0,withdrawPsd不为空串或null</li>
	 * <li>插入表T6034</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param funds
	 *            提现金额
	 * @param withdrawPsd
	 *            交易密码
	 * @param cardNumber
	 *            银行卡号
	 * @throws ParameterException
	 *             银行卡号有误
	 * @throws LogicalException
	 *             交易密码错误
	 * @throws SQLException
	 *             操作数据库异常
	 */
	public abstract void withdraw(BigDecimal funds, String withdrawPsd,
			int cardId) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 添加银行卡
	 * </dl>
	 * <dl>
	 * <ol>
	 * <li>判断参数均不能为空，银行卡有特殊规则</li>
	 * <li>插入T6024表</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param bank
	 *            开户银行
	 * @param bankAddr
	 *            开户银行地址
	 * @param cardNumber
	 *            银行卡号
	 * @param branchBank
	 *            开户支行
	 * @throws ParameterException
	 *             银行卡号错误
	 * @throws SQLException
	 *             操作数据库异常
	 */
	public abstract void addBankCard(String bank, String bankAddr,
			String branchBank, String cardNumber) throws Throwable;

	/**
	 * 获取银行名称
	 */
	public abstract String[] getBanks();

	/**
	 * 获取实名认证状态
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean getVerifyStatus() throws Throwable;
}
