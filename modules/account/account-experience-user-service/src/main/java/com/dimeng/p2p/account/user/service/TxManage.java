package com.dimeng.p2p.account.user.service;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5023;
import com.dimeng.p2p.S50.enums.T5023_F02;
import com.dimeng.p2p.S61.entities.T6118;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.account.user.service.entity.Order;
import com.dimeng.p2p.account.user.service.entity.OrderXxcz;

public abstract interface TxManage extends Service {

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
	public abstract int withdraw(BigDecimal funds, String withdrawPsd,
			int cardId, T6101_F03 f03,BigDecimal poundage,boolean txkcfs) throws Throwable;

	/**
	 * 检查交易密码是否正确
	 * @param withdrawPsd
	 * @return
	 * @throws Throwable
	 */
    public abstract boolean checkWithdrawPassword(String withdrawPsd) throws Throwable;
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
	 * 获取实名认证状态
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean getVerifyStatus() throws Throwable;
	/**
	 * 获取交易密码的认证状态
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean getVerifyTradingPsw() throws Throwable;
	
	/**
	 * 获取当前用户的认证实体
	 * @return
	 * @throws Throwable
	 */
	public abstract T6118 getVerifyEntity() throws Throwable;
	
	/**
     * 获取当前用户的认证实体
     * @return
     * @throws Throwable
     */
    public abstract T6118 getVerifyEntity(int userId) throws Throwable;

	/**
	 * 
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Order> search(Paging paging) throws Throwable;
    
    /**
     * 获取未完成的线下充值订单
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<OrderXxcz> searchXxcz(Paging paging)
        throws Throwable;

	public abstract int withdrawHdw(BigDecimal funds, String withdrawPsd,
			int cardId, T6101_F03 f03) throws Throwable;
	
	/**
     * 未完成充值记录数
     * @return int
     * @throws Throwable
     */
    int getCount() throws Throwable;
    
    /**
     * @param F02 功能说明类型
     * @throws Throwable
     */
    T5023 getT5023(T5023_F02 F02) throws Throwable;
    
    /**
     * 未完成的线下充值记录数
     * @return int
     * @throws Throwable
     */
    int getXxczCount() throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;
}
