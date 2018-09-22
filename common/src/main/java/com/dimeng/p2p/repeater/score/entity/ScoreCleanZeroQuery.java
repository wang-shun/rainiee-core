/*
 * 文 件 名:  CleanZeroQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月14日
 */
package com.dimeng.p2p.repeater.score.entity;

/**
 * <积分清零设置查询>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月14日]
 */
public abstract interface ScoreCleanZeroQuery
{
    /**
     * 开始时间
     */
    public abstract String getStartTime();

    /**
     * 结束时间
     */
    public abstract String getEndTime();
    
}
