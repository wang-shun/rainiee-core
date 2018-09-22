package com.dimeng.p2p.modules.financial.console.service.query;

import java.sql.Timestamp;

public abstract interface TransferFinishQuery {
	
	/**
	 * 债权ID，匹配查询.
	 * 
	 * @return {@code int}小于等于零无效.
	 */
	public abstract int getCreditorId();
	
	/**
	 * 标题，模糊查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getTitle();

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
