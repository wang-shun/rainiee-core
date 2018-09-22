/*
 * 文 件 名:  BuyBadClaimRecordQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年7月04日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.sql.Timestamp;

/**
 * <不良债权购买记录查询>
 * <功能详细描述>
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年7月04日]
 */
public interface BuyBadClaimRecordQuery
{
    
    /**
     * 用户ID，匹配查询.
     * 
     * @return {@code int}小于等于零无效.
     */
    public abstract int getUserId();
    
    /**
     * 债权编号
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getCreditNo();
    
    /**
     * 借款标题
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getLoanTitle();
    
    /**
     * 购买开始时间
     */
    public abstract Timestamp getStartTime();
    
    /**
     * 购买结束时间
     */
    public abstract Timestamp getEndTime();
}
