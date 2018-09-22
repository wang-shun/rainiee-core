package com.dimeng.p2p.modules.financial.console.service.query;

import java.sql.Timestamp;

public abstract interface TransferDshQuery {
	

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
	 * 债权ID，匹配查询.
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getCreditorId();
	
	/**
	 * 债权转让者，模糊查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getCreditorOwner();

	/**
	 * 标的ID，匹配查询.
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getLoanId();
	
	/**
	 * 借款标题，模糊查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getLoanTitle();
	
}
