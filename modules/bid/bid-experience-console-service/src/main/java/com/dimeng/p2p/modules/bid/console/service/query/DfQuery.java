/*
 * 文 件 名:  DfQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  beiweiyuan
 * 修改时间:  2016年6月27日
 */
package com.dimeng.p2p.modules.bid.console.service.query;

import java.sql.Timestamp;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  beiweiyuan
 * @version  [版本号, 2016年6月27日]
 */
public interface DfQuery
{
    /**
     * 账号
     * 
     * @return 账号.
     */
    public abstract String getName();
    
    /**
     * 垫付时间,大于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getAdvanceTimeStart();
    
    /**
     * 垫付时间,小于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getAdvanceTimeEnd();
    
}
