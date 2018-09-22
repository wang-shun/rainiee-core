/*
 * 文 件 名:  InterestRateQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月8日
 */
package com.dimeng.p2p.modules.activity.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S63.enums.T6342_F04;

/**
 * 加息券/红包查询条件
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年10月8日]
 */
public interface ActivityCountQuery
{
    /**
     * 用户登录名
     * <功能详细描述>
     * @return
     */
    public String userName();
    
    /**
     * 状态
     * WSY("未使用"), YSY("已使用"), YGQ("已过期")
     * @return
     */
    public String status();
    
    /**
     * 活动编号
     * <功能详细描述>
     * @return
     */
    public String activityNum();
    
    /**
     * 标的编号
     * <功能详细描述>
     * @return
     */
    public String loanNum();
    
    /**
     * 赠送开始时间
     * <功能详细描述>
     * @return
     */
    public Timestamp presentDateBegin();
    
    /**
     * 赠送结束时间
     * <功能详细描述>
     * @return
     */
    public Timestamp presentDateEnd();
    
    /**
     * 过期开始时间
     * <功能详细描述>
     * @return
     */
    public Timestamp outOfDateBegin();
    
    /**
     * 过期结束时间
     * <功能详细描述>
     * @return
     */
    public Timestamp outOfDateEnd();
    
    /**
     * 使用开始时间
     * <功能详细描述>
     * @return
     */
    public Timestamp useDateBegin();
    
    /**
     * 使用结束时间
     * <功能详细描述>
     * @return
     */
    public Timestamp useDateEnd();
    
}
