package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

public abstract interface FkshQuery {

	/**
	 * 借款账号， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getAccount();

	/**
	 * 满标时间， 大于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getStartExpireDatetime();

	/**
	 * 满标时间， 小于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getEndExpireDatetime();

	/**
	 * 借款类型
	 */
	public abstract int getType();

	/**
	 * 债权ID
	 * 
	 * @return
	 */
	public String getZqId();
	
	/**
     * <标状态>
     * @return
     */
    public String getStatus();
}
