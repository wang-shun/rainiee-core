/*
 * 文 件 名:  JxqClearCountQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月10日
 */
package com.dimeng.p2p.modules.activity.console.service.query;

import java.sql.Timestamp;

/**
 * 加息券结算统计查询条件
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [v3.1.2, 2015年10月10日]
 */
public interface JxqClearCountQuery
{
    /**
     * 用户名
     * <功能详细描述>
     * @return
     */
    public String userName();
    
    /**
     * 标的编号
     * <功能详细描述>
     * @return
     */
    public String loanNum();
    
    /**
     * 开始时间
     * <功能详细描述>
     * @return
     */
    public Timestamp startTime();
    
    /**
     * 结束时间
     * <功能详细描述>
     * @return
     */
    public Timestamp endTime();
    
    /**
     * 状态：DF-待付|YF-已付
     * <功能详细描述>
     * @return
     */
    public String status();
}
