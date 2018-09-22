package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6130_F09;

public abstract interface TxglRecordQuery {

	/**
	 * 用户名， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getYhm();

	/**
	 * 银行卡账号， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getYhkh();

	/**
	 * 提现银行， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract int getBankId();

	/**
	 * 提现时间， 大于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getStartExtractionTime();

	/**
	 * 提现时间， 小于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getEndExtractionTime();

	/**
	 * 状态
	 */
	public abstract T6130_F09 getStatus();

	/**
	 * 用户类型
	 */
	public abstract T6110_F06 getUserType();

	/**
	 * 流水号
	 */
	public abstract String getLsh();

}
