package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;

/**
 * 黑名单列表
 * @author gongliang
 *
 */
public class BlacklistDetails
{
    /**
     * 用户名
     */
    public String userName;
    
    /**
     * 真实姓名
     */
    public String realName;
    
    /**
     * 拉黑详情
     */
    public String registrationDesc;
    
    /**
    * 申请借款（笔）
    */
    public int applyLoanCount;
    
    /**
     * 成功借款（笔）
     */
    public int sucLoanCount;
    
    /**
     * 还清借款（笔）
     */
    public int payOffCount;
    
    /**
     * 信用额度（元）
     */
    public BigDecimal creditLine = BigDecimal.ZERO;
    
    /**
     * 借款总额（元）
     */
    public BigDecimal loanMoney = BigDecimal.ZERO;
    
    /**
     * 待还本息（元））
     */
    public BigDecimal borrowingLiability = BigDecimal.ZERO;
    
    /**
     * 逾期金额（元）
     */
    public BigDecimal overAmount = BigDecimal.ZERO;
    
    /**
     * 逾期次数（次）
     */
    public int overdueCount;
    
    /**
     * 严重逾期（笔）
     */
    public int seriousOverdue;
    
}
