package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.TradingType;

public abstract interface YhjyjlRecordQuery {

	/**
	 * 类型明细， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract TradingType getType();

	/**
	 * 交易时间， 大于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getStartPayTime();

	/**
	 * 交易时间， 小于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getEndPayTime();

	/**
	 * 用户ID
	 * 
	 * @return
	 */
	public abstract int getId();

}
