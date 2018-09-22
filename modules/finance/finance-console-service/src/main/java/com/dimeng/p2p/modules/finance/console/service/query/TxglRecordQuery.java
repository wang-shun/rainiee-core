package com.dimeng.p2p.modules.finance.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.WithdrawStatus;

public abstract interface TxglRecordQuery {

	/**
	 * 流水号，匹配查询
	 * 
	 * @return {@link int}空值无效
	 */
	public abstract int getLsh();
	
	/**
	 * 用户名， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getYhm();
	
	/**
	 * 姓名， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getXm();
	
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
	public abstract WithdrawStatus getStatus();

}
