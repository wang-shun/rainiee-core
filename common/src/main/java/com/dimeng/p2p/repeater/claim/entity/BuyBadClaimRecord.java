package com.dimeng.p2p.repeater.claim.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 不良债权购买记录实体类
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年7月04日]
 */
public class BuyBadClaimRecord implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7263745058428368920L;
    
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
