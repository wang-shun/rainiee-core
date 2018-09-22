package com.dimeng.p2p.modules.statistics.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SignType;

public abstract interface TransferCreditorQuery {

	/**
	 * 借款ID
	 * 
	 * @return {@link SignType}空值无效.
	 */
	public abstract String getLoanId();
	
	/**
	 * 债权ID
	 * 
	 * @return {@link SignType}空值无效.
	 */
	public abstract String getCreditorId();

	/**
	 * 卖出账户
	 * 
	 * @return {@link SignType}空值无效.
	 */
	public abstract String getSellAccount();
	
	/**
	 * 买入账户
	 * 
	 * @return {@link SignType}空值无效.
	 */
	public abstract String getBuyAccount();

	/**
	 *申请时间范围,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getApplyTimeStart();

	/**
	 * 申请时间范围,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getApplyTimeEnd();
	
	/**
	 * 购买时间范围,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getBuyTimeStart();

	/**
	 * 购买时间范围,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getBuyTimeEnd();
	
	/**
	 * 来源
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getSource();
    
    /**
     * 卖出账户类型
     * <功能详细描述>
     * @return
     */
    public abstract String getSellUserType();
    
    /**
     * 买入账户类型
     * <功能详细描述>
     * @return
     */
    public abstract String getBuyUserType();
}
