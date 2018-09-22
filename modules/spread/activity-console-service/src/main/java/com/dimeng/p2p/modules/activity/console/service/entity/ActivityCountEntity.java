/*
 * 文 件 名:  Jxqtj.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年9月30日
 */
package com.dimeng.p2p.modules.activity.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6342_F04;

/**
 * 后台：加息券统计，红包统计 页面展示实体类
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年9月30日]
 */
public class ActivityCountEntity
{
    /**
     * 用户名
     */
    public String userName;
    
    /**
     * 用户姓名
     */
    public String realName;
    
    /**
     * 加息利率
     */
    public BigDecimal interestRate;
    
    /**
     * 赠送时间
     */
    public Timestamp presentDate;
    
    /**
     * 过期时间
     */
    public Timestamp outOfDate;
    
    /**
     * 活动编号
     */
    public String activityNum;
    
    /**
     * 活动名称
     */
    public String activityName;

    /**
     * 活动类型
     */
    public T6340_F04 activityType;
    
    /**
     * 活动状态 
     */
    public T6342_F04 status;

    /**
     * 体验金状态
     */
    public T6103_F06 expStatus;
    /**
     * 使用日期
     */
    public Timestamp useDate;
    
    /**
     * 标的编号
     */
    public String loanNum;

    /**
     * 预计收益期
     */
    public String expectedTerm;

    /**
     * 实际收益期
     */
    public String actTerm;
}
