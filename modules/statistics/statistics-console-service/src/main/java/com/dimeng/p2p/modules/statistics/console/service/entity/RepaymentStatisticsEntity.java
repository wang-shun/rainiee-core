package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * 还款统计表
 */
public class RepaymentStatisticsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	    
    /**
     * 借款ID，T6230.F25
     */
	public String id;
	/**
	 * 借款标题
	 */
	public String title;
	/**
	 * 借款账号
	 */
	public String account;
	/**
	 * 账户类型(个人、企业、机构)
	 */
	public String accountType;
	/**
	 * 担保机构
	 */
	public String guaranteeOrg;
	/**
	 * 合约还款日期
	 */
	public Timestamp shouldTheDate;
	/**
	 * 状态
	 */
	public String state;
	/**
	 * 科目
	 */
	public String subject;
	/**
	 * 金额
	 */
	public BigDecimal repaymentPrice = BigDecimal.ZERO;
	/**
	 * 实际还款日期
	 */
	public Timestamp actualDate;
    
    /**
     * 借款人姓名
     */
    public String loanName;
    
    /**
     * 借款期数
     */
    public String loandeadline;
    
    /**
     * 逾期天数
     */
    public int collectionNumber;

    /**
     * 借款Id
     * 
     */
    public int loanRecordId;
    
    /**
     * 帐户Id
     */
    public int userId;
    
    /**
     * 逾期本金（元）
     */
    public BigDecimal overdueAmount = new BigDecimal(0);
    
    /**
     * 逾期利息（元）
     */
    public BigDecimal overdueInterest = new BigDecimal(0);
    
    /**
     * 逾期罚息（元）
     */
    public BigDecimal overduePenalty = new BigDecimal(0);
    
    /**
     * 垫付方式
     */
    public String paymentType;
    
    /**
     * 垫付金额
     */
    public BigDecimal paymentAmount = new BigDecimal(0);
    
    /**
     * 垫付时间
     */
    public Timestamp paymentDate;

}
