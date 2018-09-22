package com.dimeng.p2p.modules.financing.front.service.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.p2p.S60.enums.T6036_1_F04;
import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.OverdueStatus;
import com.dimeng.p2p.common.enums.RepaymentPeriod;
import com.dimeng.p2p.common.enums.RepaymentType;

/**
 * 借款标信息
 */
public class CreditInfo {

	/**
	 * 借款表ID
	 */
	public int id;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 借款描述.
	 */
	public String goalDesc;
	/**
	 * 信用等级
	 */
	public CreditLevel creditLevel;
	/**
	 * 贷款金额
	 */
	public BigDecimal amount = new BigDecimal(0);
	/**
	 * 还需金额
	 */
	public BigDecimal remainAmount = new BigDecimal(0);

	/**
	 * 年化利率
	 */
	public double rate;

	/**
	 * 进度
	 */
	public double progress;
	/**
	 * 贷款期限 (月)
	 */
	public int term;
	/**
	 * 贷款标状态
	 */
	public CreditStatus status;
	/**
	 * 借款类型
	 */
	public CreditType creditType;
	/**
	 * 还款方式
	 */
	public RepaymentType repaymentType;
	/**
	 * 下一合约还款日
	 * 
	 * @return
	 */
	public Date nextRepayDate;
	/**
	 * 剩余期数
	 * 
	 * @return
	 */
	public int remainTerms;
	/**
	 * 待还本息金额
	 */
	public BigDecimal toRepaymentAmount = new BigDecimal(0);
	/**
	 * 还清时间
	 */
	public Timestamp payedDate;
	/**
	 * 每份金额
	 */
	public BigDecimal perAmount = new BigDecimal(0);
	/**
	 * 借款人登录名
	 * 
	 */
	public String userAccountName;
	/**
	 * 借款人ID
	 */
	public int userId;

	/**
	 * 借款用途
	 */
	public String purpose;

	/**
	 * 月还款本息
	 */
	public BigDecimal yhkAmount = new BigDecimal(0);
	/**
	 * 还款周期
	 */
	public RepaymentPeriod repaymentPeriod;
	/**
	 * 可用余额
	 */
	public BigDecimal kyMoney = new BigDecimal(0);
	/**
	 * 满标时间
	 */
	public Timestamp mbTime;
	/**
	 * 审核时间
	 */
	public Timestamp shTime;
	/**
	 * 筹款结束时间
	 */
	public Timestamp jsTime;
	/**
	 * 是否逾期
	 */
	public OverdueStatus overdueStatus;
	/**
	 * 垫付时间
	 */
	public Timestamp dfTime;
	/**
	 * 封面图片
	 */
	public String fmtp;
	/**
	 * 是否推荐
	 */
	public T6036_1_F04 sftj;
	/**
	 * 剩余时间
	 */
	public int sysj;
}
