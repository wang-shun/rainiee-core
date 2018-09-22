package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Date;

public abstract interface Near30Query {
	
	/**
	 * 用户名，模糊查询.
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getUserName();
	
	/**
	 * 借款标标题，模糊查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getLoanRecordTitle();

	/**
	 * 下一还款时间,大于等于查询.
	 * 
	 * @return {@link Date}null无效.
	 */
	public abstract Date getCreateTimeStart();

	/**
	 * 下一还款时间,小于等于查询.
	 * 
	 * @return {@link Date}null无效.
	 */
	public abstract Date getCreateTimeEnd();
}
