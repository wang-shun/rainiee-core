package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

public abstract interface DzqmQuery {


	/**
	 * 用户名
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getUserName();
	
	/**
	 * 姓名/企业名称
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * 客户编号
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getCode();
	
	/**
	 * 标的编号
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getBidCode();
	
	/**
	 * 交易号
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getTradeCode();
	
	/**
	 * 合同标题
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getHtTitle();
	
	
	/**
	 * 合同编号
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getHtCode();
	
	/**
	 * 客户类型
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getUserType();
	
	/**
	 * 合同类型
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getHtType();
	
	/**
	 * 状态
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getQmState();
	
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
