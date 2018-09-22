/*
 * 文 件 名:  PlatformRiskControlEntity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年3月18日
 */
package com.dimeng.p2p.repeater.policy.entity;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 平台风险管控实体
 * 
 * @author xiaoqi
 * @version [版本号, 2016年3月18日]
 */
public class PlatformRiskControlEntity extends AbstractEntity {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 6031184310380251126L;

	/**
	 * 累计代偿金额
	 */
	public BigDecimal totalAdvancedAmount = BigDecimal.ZERO;

	/**
	 * 最大单户借款余额占比
	 */
	public BigDecimal maxUserLoanBalanceProportion = BigDecimal.ZERO;

	/**
	 * 最大10户借款余额占比
	 */
	public BigDecimal maxTenUsersLoanBalancePropertion = BigDecimal.ZERO;

	/**
	 * 借款逾期金额
	 */
	public BigDecimal loanOverdueBalanceAmount = BigDecimal.ZERO;

	/**
	 * 借贷逾期率
	 */
	public BigDecimal loanOverdueBalanceRate = BigDecimal.ZERO;

	/**
	 * 借贷坏账率
	 */
	public BigDecimal loanBadDebtRate = BigDecimal.ZERO;
}
