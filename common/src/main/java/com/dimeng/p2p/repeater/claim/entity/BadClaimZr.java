/*
 * 文 件 名:  BadClaimZr.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月14日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dimeng.p2p.S62.enums.T6230_F10;

/**
 * <不良债权转让>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月14日]
 */
public class BadClaimZr implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5050972332262484257L;
    
    /**
     * 用户Id
     */
    public int userId;
    
    /**
     * 标Id
     */
    public int bidId;
    
    /**
     * 借款编号
     */
    public String bidNo;
    
    /**
     * 标的属性
     */
    public String loanAttribute;
    
    /**
     * 借款标题
     * 
     */
    public String loanTitle;
    
    /**
     * 借款账户
     */
    public String loanName;
    
    /**
     * 借款金额
     */
    public BigDecimal loanAmount = BigDecimal.ZERO;
    
    /**
     * 还款方式
     */
    public T6230_F10 hkfs;
    
    /**
     * 年化利率
     */
    public BigDecimal yearRate = BigDecimal.ZERO;
    
    /**
     * T6252 id
     */
    public int periodId;
    
    /**
     * 剩余期数
     */
    public int syPeriod;
    
    /**
     * 总期数
     */
    public int zPeriods;
    
    /**
     * 逾期时间
     */
    public java.util.Date dueTime;
    
    /**
     * 逾期天数（天）
     */
    public int lateDays;
    
    /**
     * 本期应还金额（元）:应还金额
     */
    public BigDecimal principalAmount = BigDecimal.ZERO;
    
    /**
     * 待还金额（元）
     */
    public BigDecimal dhAmount = BigDecimal.ZERO;
    
    /**
     * 债权价值（元）
     */
    public BigDecimal claimAmount = BigDecimal.ZERO;
    
}
