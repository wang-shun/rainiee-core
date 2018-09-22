package com.dimeng.p2p.modules.statistics.console.service.query;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SignType;

public abstract interface InvestmentQuery {

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
	 * 放款时间,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getLoanTimeStart();

	/**
	 * 放款时间,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getLoanTimeEnd();
	
	/**
	 * 完结日期,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getFinishTimeStart();

	/**
	 * 完结日期,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getFinishTimeEnd();

	/**
	 * 投资账户
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getInvestAccoun();

	    /**
     * 本金范围,大于等于查询.
     * 
     * @return 
     */
    public abstract BigDecimal getInvestPriceStart();

	    /**
     * 本金范围,小于等于查询.
     * 
     * @return 
     */
    public abstract BigDecimal getInvestPriceEnd();
    
    /**
	 * 来源
	 * 
	 * 描述：
	 * 
	 * @return
	 */
	public abstract String getSource();

	/**
	 * 投资方式
	 *
	 * 描述：
	 *
	 * @return
	 */
	public abstract String getBidWay();
    
    /**
     * 借款账户类型
     * <功能详细描述>
     * @return
     */
    public abstract String getLoanUserType();
    
    /**
     * 投资账户类型
     * <功能详细描述>
     * @return
     */
    public abstract String getInvestUserType();
}