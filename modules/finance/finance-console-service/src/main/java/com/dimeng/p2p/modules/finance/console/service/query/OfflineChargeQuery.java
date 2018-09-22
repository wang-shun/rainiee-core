package com.dimeng.p2p.modules.finance.console.service.query;

import java.sql.Date;

import com.dimeng.p2p.S70.enums.T7049_F05;

/**
 * 线下充值记录查询
 */
public abstract interface OfflineChargeQuery {

	/**
	 * 账号， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getAccount();

	/**
	 * 开始时间， 大于等于查询
	 * 
	 * @return {@link Date}空值无效
	 */
	public abstract Date getCreateStartDate();

	/**
	 * 结束时间，小于等于查询
	 * 
	 * @return {@link Date}空值无效
	 */
	public abstract Date getCreateEndDate();

	/**
	 * 充值状态
	 */
	public abstract T7049_F05 getStatus();
}
