package com.dimeng.p2p.modules.finance.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.ChargeStatus;

public abstract interface CzglRecordQuery {

	/**
	 * 充值单号， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getSerialNumber();

	/**
	 * 用户名， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getLoginName();

	/**
	 * 充值时间， 大于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getStartRechargeTime();

	/**
	 * 充值时间， 小于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getEndRechargeTime();

	/**
	 * 充值方式， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getPayType();

	/**
	 * 充值状态， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract ChargeStatus getStatus();

}
