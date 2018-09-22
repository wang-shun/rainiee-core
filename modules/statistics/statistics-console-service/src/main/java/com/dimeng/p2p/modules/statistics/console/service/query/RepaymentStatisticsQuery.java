package com.dimeng.p2p.modules.statistics.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SignType;

public abstract interface RepaymentStatisticsQuery {

	/**
	 * 借款标标题，模糊查询.
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getLoanTitle();

	/**
	 * 借款ID
	 * 
	 * @return {@link SignType}空值无效.
	 */
	public abstract String getId();

	/**
	 *合约还款时间,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getShouldTheDateStart();

	/**
	 * 合约还款时间,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getShouldTheDateEnd();
	
	/**
	 * 实际还款日期,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getActualDateStart();

	/**
	 * 实际还款日期,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getActualDateEnd();

	/**
	 * 状态
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getState();

	/**
	 * 科目
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getSubject();
	
	/**
	 * 借款账户
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getLoanAccount();
    
    /**
     * 账户类型
     * <功能详细描述>
     * @return
     */
    public abstract String getAccountState();
    
    /**
     * 逾期天数最小
     * <功能详细描述>
     * @return
     */
    public abstract int getOverdueDaysMin();
    
    /**
     * 逾期天数最大
     * <功能详细描述>
     * @return
     */
    public abstract int getOverdueDaysMax();

    /**
     * 垫付方式
     * <功能详细描述>
     * @return
     */
    public abstract String getPaymentType();
    
    /**
     * 担保机构
     * <功能详细描述>
     * @return
     */
    public abstract String getGuaranteeAgencies();
    
    /**
     *垫付时间,大于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getPaymentDateStart();
    
    /**
     * 垫付时间,小于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getPaymentDateEnd();
}
