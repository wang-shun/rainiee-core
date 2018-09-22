/*
 * 文 件 名:  DshQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月15日
 */
package com.dimeng.p2p.repeater.claim.query;

import java.sql.Date;

import com.dimeng.p2p.S62.enums.T6264_F04;

/**
 * <不良债权待审核查询>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月15日]
 */
public interface DshQuery extends DzrQuery
{
    
    /**
     * 债权编号
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getClaimNo();
    
    /**
     * 状态
     * 
     * @return {@link String}空值无效.
     */
    public abstract T6264_F04 getState();
    
    /**
     * 申请开始时间
     */
    public abstract Date getStartTime();
    
    /**
     * 申请结束时间
     */
    public abstract Date getEndTime();
    
}
