package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * 投资统计表
 */
public class InvestmentListEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 借款ID，T6230.F01
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
	 * 借款金额
	 */
	public BigDecimal loanPrice = BigDecimal.ZERO;
	/**
	 * 年化利率
	 */
	public BigDecimal annualRate = BigDecimal.ZERO;
	/**
	 * 还款方式
	 */
	public String wayOfRepayment;
	/**
	 * 期限
	 */
	public String timeLimit;
	/**
	 * 放款时间
	 */
	public Timestamp loanTime;
	/**
	 * 完结日期
	 */
	public Timestamp endDate;
	/**
	 * 投资账号
	 */
	public String investAccoun;
	/**
	 * 投资姓名
	 */
	public String investName;
	/**
	 * 投资本金
	 */
	public BigDecimal investPrice = BigDecimal.ZERO;
	/**
	 * 投资时间
	 */
	public Timestamp investDate;
	
	/**
	 * 来源
	 */
	public String source;

	/**
	 * 投资方式，自动，手动
	 */
	public String bidWay;
	
    /**
     * 投资账户类型(个人、企业、机构)
     */
    public String investAccountType;

}