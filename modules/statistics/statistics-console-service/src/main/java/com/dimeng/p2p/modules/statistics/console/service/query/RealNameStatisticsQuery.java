/*
 * 文 件 名:  RealNameStatisticsQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月5日
 */
package com.dimeng.p2p.modules.statistics.console.service.query;

import java.sql.Timestamp;

/**
 * <实名认证查询条件>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年4月5日]
 */
public interface RealNameStatisticsQuery
{
    
    /**
     * 用户名
     */
    String getUserName();
    
    /**
     * 真实姓名
     */
    String getRealName();
    
    /**
     * 认证来源
     */
    String getAuthSource();
    
    /**
     * 认证通过开始时间
     */
    Timestamp getAuthPassTimeStart();
    
    /**
     * 认证通过结束时间
     */
    Timestamp getAuthPassTimeEnd();
}
