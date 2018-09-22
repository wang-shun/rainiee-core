package com.dimeng.p2p.modules.systematic.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.ConsoleLogType;

public abstract interface OperLogQuery {
	/**
	 * 操作人
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getName();

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
    public abstract ConsoleLogType getType();
}
