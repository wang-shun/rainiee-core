package com.dimeng.p2p.modules.statistics.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SignType;

public abstract interface DFStatisticsQuery {


	/**
	 * 借款ID
	 * 
	 * @return {@link SignType}空值无效.
	 */
	public abstract String getLoanId();
	
	/**
	 * 垫付账户名
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getDfAccount();
	
	/**
	 * 垫付机构名称
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getDfAccountName();
	
	/**
	 *垫付时间范围,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getDfTimeStart();

	/**
	 * 垫付时间范围,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getDfTimeEnd();
	
	/**
	 * 返还时间范围,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getReMoneyTimeStart();

	/**
	 * 返还时间范围,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getReMoneyTimeEnd();

	/**
	 * 借款用户名
	 *
	 * 描述：
	 *
	 * @return
	 */
	public abstract String getJKAccount();


}
