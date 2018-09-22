package com.dimeng.p2p.modules.finance.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.RepaymentType;

public class Jkr implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 借款ID
	 */
	public int id;
	/**
	 * 借款人
	 */
	public int userId;
	/**
	 * 借款金额
	 */
	public BigDecimal amount = new BigDecimal(0);
	/**
	 * 信用等级
	 */
	public CreditLevel level;
	/**
	 * 借款类型
	 */
	public CreditType type;
	/**
	 * 合同ID
	 */
	public int contrantId;
	/**
	 * 成交服务费
	 */
	public BigDecimal cjfwf = new BigDecimal(0);
	/**
	 * 借款人账号余额
	 */
	public BigDecimal acountAmount = new BigDecimal(0);
	/**
	 * 借款期限
	 */
	public int days;

	/**
	 * 年化利率
	 */
	public double yearRate;

	/**
	 * 放款时间
	 */
	public Timestamp fkDate;

	/**
	 * 还款方式
	 */
	public RepaymentType repayType;
	/**
	 * 没份金额
	 * 
	 */
	public BigDecimal mfje;
	/**
	 * 借款标题
	 */
	public String title;
}
