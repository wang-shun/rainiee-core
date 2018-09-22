package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.common.enums.InvestType;

public interface CjRecordQuery {
	/**
	 * 放款人
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getLoanName();

	/**
	 * 放款时间， 大于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getStartTime();

	/**
	 * 放款时间， 小于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getEndTime();

	/**
	 * 借款类型
	 */
	public abstract InvestType getType();

	/**
	 * 信用等级
	 */
	public abstract CreditLevel getLevel();

	/**
	 * 借款期限
	 */
	public abstract CreditTerm getTerm();

}
