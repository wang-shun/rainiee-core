/*
 * 文 件 名:  SubscribeBadClaim.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月15日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 
 * 可认购的不良债权实体类
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月15日]
 */
public class SubscribeBadClaim implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 债权转让id
     */
    public int id;
    
    /**
     * 标的id
     */
    public int bidId;
    
    /**
     * 债权编号
     */
    public String creditNo;
    
    /**
     * 借款标题
     */
    public String loanTitle;
    
    /**
     * 借款金额
     */
    public BigDecimal loanAmount = BigDecimal.ZERO;
    
    /**
     * 剩余期数
     */
    public int syPeriods;
    
    /**
     * 总期数
     */
    public int zPeriods;
    
    /**
     * 年化利率
     */
    public BigDecimal yearRate = BigDecimal.ZERO;
    
    /**
     * 下一个还款日
     */
    public Date nextDate;
    
    /**
     * 逾期天数
     */
    public int overdueDays;
    
    /**
     * 债权价值
     */
    public BigDecimal creditPrice = BigDecimal.ZERO;
    
    /**
     * 认购价格
     */
    public BigDecimal subscribePrice = BigDecimal.ZERO;
    
    /**
     * 认购时间
     */
    public Timestamp subscribeTime;
    
}
