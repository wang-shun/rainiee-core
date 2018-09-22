/*
 * 文 件 名:  DzrQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月14日
 */
package com.dimeng.p2p.repeater.claim.query;

import java.sql.Timestamp;

/**
 * <不良债权待转让查询>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月14日]
 */
public interface DzrQuery
{
    /**
     * 借款编号
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getBidNo();
    
    /**
     * 借款标题
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getLoanTitle();
    
    /**
     * 借款账户
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getLoanName();
    
    /**
     * 逾期天数-开始
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract String getYuqiFromDays();
    
    /**
     * 逾期天数-结束
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract String getYuqiEndDays();
    
    /**
     * 标的属性
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getLoanAttribute();
    
}
