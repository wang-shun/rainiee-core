package com.dimeng.p2p.repeater.preservation.query;

import java.sql.Timestamp;

public abstract interface AgreementSaveQuery {

    /**
     * 用户名
     * <功能详细描述>
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
     * 保全ID
     * <功能详细描述>
     * @return
     */
    public abstract String getAgreementId();
	    
    /**
     * 保全时间,大于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getAgreementTimeStart();

	    /**
     * 保全时间,小于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getAgreementTimeEnd();
    
    /**
     * 协议编号
     * <功能详细描述>
     * @return
     */
    public abstract String getAgreementNum();
    
    /**
     * 保全状态
     * <功能详细描述>
     * @return
     */
    public abstract String getAgreementState();
}
