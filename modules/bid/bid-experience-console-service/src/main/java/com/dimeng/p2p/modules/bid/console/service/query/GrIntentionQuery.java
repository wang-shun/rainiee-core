/**
 * 
 */
package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6282_F11;
import com.dimeng.p2p.common.enums.IntentionType;
import com.dimeng.p2p.common.enums.LoanIntentionState;

/**
 * @author guopeng
 * 
 */
public abstract interface GrIntentionQuery {
	/**
	 * 联系人，模糊查询.
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getUserName();

	/**
	 * 借款类型，匹配查询.
	 * 
	 * @return {@link IntentionType}空值无效.
	 */
	public abstract int getType();

	/**
	 * 所在城市省，匹配查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getSheng();

	/**
	 * 所在城市市，匹配查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getShi();

	/**
	 * 所在城市乡，匹配查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getXian();

	/**
	 * 状态，匹配查询.
	 * 
	 * @return {@link LoanIntentionState}空值无效.
	 */
	public abstract T6282_F11 getStatus();

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
