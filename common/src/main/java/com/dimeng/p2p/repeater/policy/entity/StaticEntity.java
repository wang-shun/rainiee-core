/**
 * 
 */
package com.dimeng.p2p.repeater.policy.entity;

import java.math.BigDecimal;

/**
 * 
 * <运营数据统计> <功能详细描述>
 * 
 * @author liulixia
 * @version [版本号, 2018年2月7日]
 */
public class StaticEntity {
	/**
	 * 累计交易金额
	 */
	public BigDecimal totalDealAmont = BigDecimal.ZERO;
	/**
	 * 累计交易笔数
	 */
	public int totalDealCount;

	/**
	 * 借贷余额
	 */
	public BigDecimal loanBalance = BigDecimal.ZERO;

	/**
	 * 借贷余额笔数
	 */
	public int loanBalanceCount;

	/**
	 * 累计出借人数量
	 */
	public int totalInvestment;

	/**
	 * 当前出借人数量
	 */
	public int cuurInvestment;

	/**
	 * 累计借款人数量
	 */
	public int totalLoan;

	/**
	 * 当前借款人数量
	 */
	public int cuurLoan;

	/**
	 * 最大10户借款余额占比
	 */
	public BigDecimal maxTenUsersLoanBalancePropertion = BigDecimal.ZERO;

	/**
	 * 最大单户借款余额占比
	 */
	public BigDecimal maxUserLoanBalanceProportion = BigDecimal.ZERO;

	/**
	 * 注册用户数
	 */
	public int registerCount;

	/**
	 * 借贷利息余额
	 */
	public BigDecimal loanInterestBalance = BigDecimal.ZERO;

	/**
	 * 累计赚取收益
	 */
	public BigDecimal totalProfit = BigDecimal.ZERO;

	/**
	 * 关联关系借款余额
	 */
	public BigDecimal correlationBalance = BigDecimal.ZERO;

	/**
	 * 关联关系借款笔数
	 */
	public int correlationCount;

	/**
	 * 逾期金额
	 */
	public BigDecimal overdueBalanceAmount = BigDecimal.ZERO;

	/**
	 * 金额逾期率 逾期金额//累计成交金额
	 */
	public BigDecimal amountBalanceRate = BigDecimal.ZERO;

	/**
	 * 逾期笔数
	 */
	public int overdueBalanceCount;

	/**
	 * 项目逾期率：逾期笔数/累计成交笔数
	 */
	public BigDecimal projectBalanceRate = BigDecimal.ZERO;

	/**
	 * 累计代偿金额
	 */
	public BigDecimal totalAdvancedAmount = BigDecimal.ZERO;

	/**
	 * 累计代偿笔数
	 */
	public int totalAdvancedCount;

	/**
	 * 逾期90天以上金额
	 */
	public BigDecimal overdueBalanceAmount90 = BigDecimal.ZERO;

	/**
	 * 逾期90天以上笔数
	 */
	public int overdueBalanceCount90;

}
