package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6131_F07;

public abstract interface CzglRecordQuery {

	/**
	 * 充值单号， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getSerialNumber();

	/**
	 * 用户名， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getLoginName();

	/**
	 * 充值时间， 大于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getStartRechargeTime();

	/**
	 * 充值时间， 小于等于查询
	 * 
	 * @return {@link Timestamp}空值无效
	 */
	public abstract Timestamp getEndRechargeTime();

	/**
	 * 充值方式， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getPayType();

	/**
	 * 充值状态， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract T6131_F07 getStatus();

	/**
	 * 用户类型
	 */
	public abstract T6110_F06 getUserType();

}
