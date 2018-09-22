package com.dimeng.p2p.modules.spread.console.service.entity;

import java.sql.Timestamp;

/**
 * 奖励金详情列表
 *
 */
public interface BonusQuery {
	/**
	 * 借款ID
	 */
	public String loanID();
	/**
	 * 借款账户
	 */
	public String loanUserName();
	/**
	 * 投资账户
	 */
	public String investUserName();
	/**
	 * 放款开始时间
	 */
	public Timestamp fkStartTime();
	/**
	 * 放款结束时间
	 */
	public Timestamp fkEndTime();
}
