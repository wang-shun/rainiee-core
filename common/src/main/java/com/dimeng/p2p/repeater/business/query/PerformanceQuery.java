package com.dimeng.p2p.repeater.business.query;

import java.sql.Timestamp;
/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月9日]
 */
public abstract interface PerformanceQuery {


	/**
	 * 业务员工号.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getEmployNum();
	/**
	 * 业务员姓名.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getName();
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
