package com.dimeng.p2p.modules.systematic.console.service.query;

import java.sql.Timestamp;

public abstract interface ConstantLogQuery {
	/**
	 * 常量KEY
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getKey();

	/**
	 * 常量名称
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getName();

	/**
	 * 修改时间,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getTimeStart();

	/**
	 * 修改时间,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getTimeEnd();
}
