package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.common.enums.SignType;

public abstract interface LoanQuery {

	/**
	 * 借款标标题，模糊查询.
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getLoanTitle();

	/**
	 * 借款类型ID
	 * 
	 * @return {@link SignType}空值无效.
	 */
	public abstract int getType();

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
	 * 借款账户
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * 
	 * 状态
	 * 
	 * @return
	 */
	public abstract T6230_F20 getStatus();
}
