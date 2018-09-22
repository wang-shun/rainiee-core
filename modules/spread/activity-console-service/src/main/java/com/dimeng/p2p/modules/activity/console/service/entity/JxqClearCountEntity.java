/*
 * 文 件 名:  JxqClearCountEntity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月9日
 */
package com.dimeng.p2p.modules.activity.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 
 * 加息券结算统计展示实体类
 * 
 * @author  xiaoqi
 * @version  [v3.1.2, 2015年10月9日]
 */
public class JxqClearCountEntity
{
    /**
     * 标的编号
     */
    public String loanNum;
    
    /**
     * 用户登录名
     */
    public String userName;
    
    /**
     * 用户真实姓名
     */
    public String realName;
    
    /**
     * 加息利率
     */
    public BigDecimal interestRate;
    
    /**
     * 加息奖励金额
     */
    public BigDecimal rewardAmount;
    
    /**
     * 付款时间
     */
    public Timestamp payTime;
}
