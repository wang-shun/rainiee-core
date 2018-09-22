/*
 * 文 件 名:  YzrQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.repeater.claim.query;

import java.sql.Date;

/**
 * <不良债权已转让查询>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月16日]
 */
public interface YzrQuery extends DzrQuery
{
    
    /**
     * 债权编号
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getClaimNo();
    
    /**
     * 债权接收方
     * 
     * @return {@link String}空值无效.
     */
    public abstract int getClaimReceiver();
    
    /**
     * 购买开始时间
     */
    public abstract Date getStartTime();
    
    /**
     * 购买结束时间
     */
    public abstract Date getEndTime();
}
