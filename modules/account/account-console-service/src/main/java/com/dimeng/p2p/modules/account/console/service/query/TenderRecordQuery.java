package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6230_F20;

/**
 * 信用档案查询
 * @author gongliang
 *
 */
public abstract interface TenderRecordQuery {
	
	/**
	 * 用户ID，匹配查询.
	 * 
	 * @return {@code int}小于等于零无效.
	 */
	public abstract int getUserId();
	
	/**
	 * 债权ID，匹配查询.
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getTenderRecordId();
	
	/**
	 * 债权标题，模糊查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getTenderRecordTitle();
	
	/**
	 * 注册时间,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getCreateTimeStart();

	/**
	 * 注册时间,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getCreateTimeEnd();
	
	/**
	 * 状态状态,匹配查询.
	 * 
	 * @return {@link T6230_F20}空值无效.
	 */
	public abstract T6230_F20 getTenderRecordState();
}
