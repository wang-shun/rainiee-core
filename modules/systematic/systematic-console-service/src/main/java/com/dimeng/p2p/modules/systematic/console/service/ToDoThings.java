package com.dimeng.p2p.modules.systematic.console.service;

import com.dimeng.framework.service.Service;

public abstract interface ToDoThings extends Service {

	/**
	 * 根据类型查询借款项目
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public int queryDshProCount(String type) throws Throwable;
	
	/**
	 * 查询待处理的个人借款意向
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public int queryDshOwnLoanCountCount(String type) throws Throwable;
	
	/**
	 * 查询待处理的企业借款意向
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public int queryDshEnterpriseLoanCount(String type) throws Throwable ;
	
	/**
	 * 待审核的认证信息
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public int queryDshAuthCount(String type) throws Throwable;
	
	/**
	 * 提现初审/复审数量
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public int queryTxTrialCount(String type) throws Throwable;
	
	/**
	 * 线下充值待审核数量
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public int queryunderLineChargingCount(String type) throws Throwable;
	
	/**
	 * 债权转让待审核数目
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public int queryAssignmentCount(String type) throws Throwable;
}
