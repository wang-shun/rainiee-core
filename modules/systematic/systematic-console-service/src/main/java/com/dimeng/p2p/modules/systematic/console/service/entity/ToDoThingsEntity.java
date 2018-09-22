package com.dimeng.p2p.modules.systematic.console.service.entity;


public class ToDoThingsEntity {

	/**
	 * 待审核的借款项目数量
	 */
	public int dshProCount;
	
	/**
	 * 待发布的借款项目数量
	 */
	public int dfbProCount;
	
	/**
	 * 待放款的借款项目数量
	 */
	public int dfkProCount;
	
	/**
	 * 待处理的个人借款意向数量
	 */
	public int dshOwnLoanCount;
	
	/**
	 * 待处理的企业借款意向数量
	 */
	public int dshEnterpriseLoanCount;
	
	/**
	 * 待审核的认证信息数量
	 */
	public int dshAuthCount;
	
	/**
	 * 提现初审数量
	 */
	public int txTrialCount;
	
	/**
	 * 提现复审数量
	 */
	public int txReviewCount;
	
	/**
	 * 线下充值数量
	 */
	public int underLineChargingCount;
	
	/**
	 * 债权转让数量
	 */
	public int assignmentCount;

	public int getDshProCount() {
		return dshProCount;
	}

	public void setDshProCount(int dshProCount) {
		this.dshProCount = dshProCount;
	}

	public int getDfbProCount() {
		return dfbProCount;
	}

	public void setDfbProCount(int dfbProCount) {
		this.dfbProCount = dfbProCount;
	}

	public int getDfkProCount() {
		return dfkProCount;
	}

	public void setDfkProCount(int dfkProCount) {
		this.dfkProCount = dfkProCount;
	}

	public int getDshOwnLoanCount() {
		return dshOwnLoanCount;
	}

	public void setDshOwnLoanCount(int dshOwnLoanCount) {
		this.dshOwnLoanCount = dshOwnLoanCount;
	}

	public int getDshEnterpriseLoanCount() {
		return dshEnterpriseLoanCount;
	}

	public void setDshEnterpriseLoanCount(int dshEnterpriseLoanCount) {
		this.dshEnterpriseLoanCount = dshEnterpriseLoanCount;
	}

	public int getDshAuthCount() {
		return dshAuthCount;
	}

	public void setDshAuthCount(int dshAuthCount) {
		this.dshAuthCount = dshAuthCount;
	}

	public int getTxTrialCount() {
		return txTrialCount;
	}

	public void setTxTrialCount(int txTrialCount) {
		this.txTrialCount = txTrialCount;
	}

	public int getTxReviewCount() {
		return txReviewCount;
	}

	public void setTxReviewCount(int txReviewCount) {
		this.txReviewCount = txReviewCount;
	}

	public int getUnderLineChargingCount() {
		return underLineChargingCount;
	}

	public void setUnderLineChargingCount(int underLineChargingCount) {
		this.underLineChargingCount = underLineChargingCount;
	}

	public int getAssignmentCount() {
		return assignmentCount;
	}

	public void setAssignmentCount(int assignmentCount) {
		this.assignmentCount = assignmentCount;
	}

}
