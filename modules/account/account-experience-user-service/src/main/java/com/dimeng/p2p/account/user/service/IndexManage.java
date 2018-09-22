package com.dimeng.p2p.account.user.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.account.user.service.entity.Bdlb;
import com.dimeng.p2p.account.user.service.entity.FinancialPlan;
import com.dimeng.p2p.account.user.service.entity.LoanAccount;
import com.dimeng.p2p.account.user.service.entity.LoanAccountInfo;
import com.dimeng.p2p.account.user.service.entity.Notice;
import com.dimeng.p2p.account.user.service.entity.TenderAccount;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;

public interface IndexManage extends Service {
	/**
	 * 获取用户基础信息
	 * 
	 * 
	 * @return {@link UserBaseInfo}
	 * @throws Throwable
	 */
	public UserBaseInfo getUserBaseInfo() throws Throwable;

	/**
	 * 获取用户基础信息（除了交易密码）
	 * 
	 * 
	 * @return {@link UserBaseInfo}
	 * @throws Throwable
	 */
	public UserBaseInfo getUserBaseInfoTx() throws Throwable;

	/**
	 * 获取站内公告
	 * 
	 * @return {@link Notice}
	 * @throws Throwable
	 */
	public Notice getNotice() throws Throwable;

	/**
	 * 获取贷款负债
	 * 
	 * @return {@link BigDecimal}
	 * @throws Throwable
	 */
	public BigDecimal getLoanAmount() throws Throwable;

	/**
	 * 获取投资账户
	 * 
	 * @return {@link TenderAccount}
	 * @throws Throwable
	 */
	public TenderAccount getTenderAccount() throws Throwable;

	/**
	 * 获取贷款账户
	 * 
	 * @return {@link LoanAccount}
	 * @throws Throwable
	 */
	public LoanAccount[] getLoanAccount() throws Throwable;

	/**
	 * 获取推荐的优选理财
	 * 
	 * @return {@link FinancialPlan}
	 * @throws Throwable
	 */
	public FinancialPlan getFinancialPlan() throws Throwable;

	/**
	 * 获取散标投资
	 * 
	 * @return {@link Bid}
	 * @throws Throwable
	 */
	public Bdlb[] getBids() throws Throwable;

	/**
	 * 获取待还总额
	 * 
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getUnpayTotal() throws Throwable;

	/**
	 * 得到用户的借款等级
	 * 
	 * @return
	 * @throws Throwable
	 */
	public T5127_F03 getJkdj() throws Throwable;

	/**
	 * 得到用户的投资等级
	 * 
	 * @return
	 * @throws Throwable
	 */
	public T5127_F03 getTzdj() throws Throwable;

	/**
	 * 用户的借款总金额
	 * 
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getJkje() throws Throwable;

	/**
	 * 用户的投资总金额
	 * 
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getTzje() throws Throwable;

	/**
	 * 用户的充值总金额
	 * 
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getCzze() throws Throwable;

	/**
	 * 用户的提现总金额
	 * 
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getTxze() throws Throwable;

	/**
	 * 待收本金
	 * 
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getDsbj() throws Throwable;

	/**
	 * 待收利息
	 * 
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getDslx() throws Throwable;

	/**
	 * 获取贷款账户详细信息
	 * 
	 * @return {@link LoanAccount}
	 * @throws Throwable
	 */
	public LoanAccountInfo[] getLoanAccountInfo() throws Throwable;
}
