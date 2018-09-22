package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

/**
 * 用户列表查询
 * @author gongliang
 *
 */
public abstract interface BlacklistQuery
{
    
    /**
     * 用户名称，模糊查询.
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getUserName();
    
    /**
     * 手机号码，匹配查询.
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getMsisdn();
    
    /**
     * 拉黑时间,大于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getCreateTimeStart();
    
    /**
     * 拉黑时间,小于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getCreateTimeEnd();
    
    /**
     * 真实姓名
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getRealName();
    
    /**
     * 身份证号码
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getIdCard();
}
