package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

public abstract interface DzxyQuery {


	/**
	 * 时间,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getCreateTimeStart();

	/**
	 * 时间,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getCreateTimeEnd();

	/**
	 * 借款账户
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getName();
}
