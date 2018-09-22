package com.dimeng.p2p.modules.financial.console.service.query;

import java.sql.Timestamp;

public abstract interface CreditorQuery {
	
	/**
	 * 债权ID，匹配查询.
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getCreditorId();
	
	/**
	 * 债权人，模糊查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getUserName();

	/**
	 * 借款标题，模糊查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getLoanRecordTitle();

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
    
    /**
     * 投资人类型(个人、机构、企业)
     * <功能详细描述>
     * @return
     */
    public abstract String getInvestUserType();
    
    /**
     * 债权人类型(个人、机构、企业)
     * <功能详细描述>
     * @return
     */
    public abstract String getCreditorType();
    
    /**
     * 卖出者类型(个人、机构、企业)
     * <功能详细描述>
     * @return
     */
    public abstract String getSellUserType();
    
    /**
     * 买入者类型(个人、机构、企业)
     * <功能详细描述>
     * @return
     */
    public abstract String getBugUserType();
}
