package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

public interface CjRecordQuery {

	/**
	 * 放款时间， 大于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getStartTime();

	/**
	 * 放款时间， 小于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getEndTime();

	/**
	 * 借款类型
	 */
	public abstract int getType();

	/**
	 * 用户名
	 */
	public abstract String getUserName();
	
	/**
	 * 借款编号
	 * <功能详细描述>
	 * @return
	 */
	public abstract String getLoanNum();

}
