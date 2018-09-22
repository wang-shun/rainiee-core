package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Date;

import com.dimeng.p2p.common.enums.IsHovestatus;

public abstract interface Less30Query {
	
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
	 * 催收时间,大于等于查询.
	 * 
	 * @return {@link Date}null无效.
	 */
	public abstract Date getCreateTimeStart();

	/**
	 * 催收时间,小于等于查询.
	 * 
	 * @return {@link Date}null无效.
	 */
	public abstract Date getCreateTimeEnd();
	
	/**
	 * 催收记录,匹配查询.
	 * 
	 * @return {@link String}null无效.
	 */
	public abstract IsHovestatus getState();
}
