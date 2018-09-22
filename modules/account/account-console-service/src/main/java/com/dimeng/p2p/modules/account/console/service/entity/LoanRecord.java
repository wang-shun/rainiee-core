package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;

/**
 * 借款记录
 * @author gongliang
 *
 */
public class LoanRecord
{
    /**
     * 借款ID
     */
    public int loanRecordId;
    
    /**
     * 借款类型
     */
    public String loanRecordType;
    
    /**
     * 借款标题
     */
    public String loanRecordTitle;
    
    /**
     * 年化利率
     */
    public double yearRate;
    
    /**
     * 投资金额
     */
    public BigDecimal amount = new BigDecimal(0);
    
    /**
     * 借款金额
     */
    public BigDecimal loanAmount = new BigDecimal(0);
    
    /**
     * 期限
     */
    public int deadline;
    
    /**
     * 借款时间
     */
    public Timestamp loanRecordTime;
    
    /**
     * 逾期次数
     */
    public int overdueCount;
    
    /**
     * 严重逾期笔数
     */
    public int seriousOverdue;
    
    /**
     * 标的状态
     */
    public T6230_F20 loanRecordState;
    
    /**
     * 是否按天借款
     */
    public T6231_F21 dayBorrowFlg;
    
    /**
     * 标的编号
     */
    public String loanNum;
}
