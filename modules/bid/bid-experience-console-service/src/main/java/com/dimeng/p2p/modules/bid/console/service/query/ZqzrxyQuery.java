package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

public abstract interface ZqzrxyQuery {


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
	 * 转让账户
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getName();
}
