package com.dimeng.p2p.modules.bid.user.service.query;

import java.sql.Timestamp;
import java.util.Date;

import com.dimeng.p2p.common.enums.QueryType;

public interface BackOffQuery {

	/**
	 * 查询类型
	 * 
	 * @return {@link QueryType}
	 */
	public abstract QueryType getQueryType();
	/**
	 * 查询时间从
	 * 
	 * @return {@link Timestamp}
	 */
	public abstract Date getTimeStart();
	/**
	 * 查询时间到
	 * 
	 * @return {@link Timestamp}
	 */
	public abstract Date getTimeEnd();
}
