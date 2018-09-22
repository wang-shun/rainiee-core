/*
 * 文 件 名:  DonationQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月10日
 */
package com.dimeng.p2p.repeater.donation.query;

import java.sql.Timestamp;

/**
 * 投资记录查询
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月10日]
 */
public interface DonationQuery
{
    /**
     * 标ID
     * <功能详细描述>
     * @return
     */
    public abstract int getBidId();
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
     * <用户ID>
     * <功能详细描述>
     * @return
     */
    public abstract int getUserId();
    
}
