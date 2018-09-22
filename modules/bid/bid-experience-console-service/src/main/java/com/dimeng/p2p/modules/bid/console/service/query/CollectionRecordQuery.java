package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S71.enums.T7152_F04;

public abstract interface CollectionRecordQuery {

	/**
	 * 借款用户，模糊查询.
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getUserName();

	/**
	 * 催收方式，匹配查询.
	 * 
	 * @return {@link T7152_F04}空值无效.
	 */
	public abstract T7152_F04 getType();

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
}
