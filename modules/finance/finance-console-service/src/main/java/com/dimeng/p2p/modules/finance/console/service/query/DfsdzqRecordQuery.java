package com.dimeng.p2p.modules.finance.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.RestoreStatus;

public abstract interface DfsdzqRecordQuery {

	/**
	 * 还款状态， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract RestoreStatus getStatus();

	/**
	 * 垫付时间， 大于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getStartPaymentTime();

	/**
	 * 垫付时间， 小于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getEndPaymentTime();
	/**
	 * 用户名
	 */
	public abstract String getLoginName();
	/**
	 * 债权ID
	*
	* @return
	 */
	public abstract int getZqId();
}
