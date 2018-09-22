package com.dimeng.p2p.repeater.policy.entity;

import java.io.Serializable;

/**
 * 投资/借款用户分布
 */
public class InvestmentLoanEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 投资用户
	 */
	public int totalInvestment;
	/**
	 * 借款用户
	 */
	public int totalLoan;
}
