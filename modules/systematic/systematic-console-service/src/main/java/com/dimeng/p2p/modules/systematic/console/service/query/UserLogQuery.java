package com.dimeng.p2p.modules.systematic.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.FrontLogType;

public abstract interface UserLogQuery {
	/**
	 * 用户名称.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getAccountName();

	/**
	 * 登录时间,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getCreateTimeStart();

	/**
	 * 登录时间,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getCreateTimeEnd();
    
    /**
     * 日志类型
     * <功能详细描述>
     * @return
     */
    public abstract FrontLogType getType();
}
