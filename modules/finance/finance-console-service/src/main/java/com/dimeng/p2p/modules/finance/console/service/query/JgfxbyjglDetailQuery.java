package com.dimeng.p2p.modules.finance.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.TradeType;

public interface JgfxbyjglDetailQuery {

	/**
	 * 类型明细， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract TradeType getType();

	/**
	 * 时间， 大于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getStartDateTime();

	/**
	 * 时间， 小于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getEndDatetime();

	/**
	 * 机构ID
	 * 
	 * @return
	 */
	public abstract int getId();

	/**
	 * 用户名
	 */
	public abstract String getUserName();

}
