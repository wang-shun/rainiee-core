package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 还款信息实体类
 * 
 */
public class ForwardRepayInfo
{
    /**
     * 还款标ID
     */
    public int loanID;
    
    /**
     * 提前还款总需
     */
    public BigDecimal loanTotalMoney = new BigDecimal(0);
    
    /**
     * 用户账户余额
     */
    public BigDecimal accountAmount = new BigDecimal(0);
    
    /**
     * 当期应还总本息
     */
    public BigDecimal loanMustMoney = new BigDecimal(0);
    
    /**
     * 剩余本金
     */
    public BigDecimal sybj = new BigDecimal(0);
    
    /**
     * 提前还款手续费
     */
    public BigDecimal loanManageAmount = new BigDecimal(0);
    
    /**
     * 违约金
     */
    public BigDecimal loanPenalAmount = new BigDecimal(0);
    
    /**
     * 当前期号
     */
    public int number;
    
    /**
     * 剩余利息
     */
    public BigDecimal sylx = new BigDecimal(0);
}
