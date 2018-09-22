package com.dimeng.p2p.modules.account.recharge.service.query;

import java.sql.Date;

/**
 * 线下充值记录查询
 */
public abstract interface OfflineChargeExtendsQuery extends OfflineChargeQuery {
	
	/**
	 * 建立操作人，模糊查询
	 * 
	 * @return {@link Date}空值无效
	 */
	public abstract String getCreateAccount();
	
	/**
	 * 审核人，模糊查询
	 * 
	 * @return {@link Date}空值无效
	 */
	public abstract String getAuditor();

	/**
	 * 审核开始时间， 大于等于查询
	 * 
	 * @return {@link Date}空值无效
	 */
	public abstract Date getAuditorStartDate();

	/**
	 * 审核结束时间，小于等于查询
	 * 
	 * @return {@link Date}空值无效
	 */
	public abstract Date getAuditorEndDate();
}
