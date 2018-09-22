package com.dimeng.p2p.modules.financial.console.service.query;

import java.sql.Timestamp;

public abstract interface TransferProceedQuery {
	

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
