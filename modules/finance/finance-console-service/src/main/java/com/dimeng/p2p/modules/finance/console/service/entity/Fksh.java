package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.InvestType;

public class Fksh {

	/**
	 * ID
	 */
	public int id;
	/**
	 * 债权ID
	 */
	public int zqId;

	/**
	 * 用户Id
	 */
	public int userId;
	/**
	 * 借款标题
	 */
	public String title;
	/**
	 * 借款账户
	 */
	public String accountName;
	/**
	 * 信用等级
	 */
	public CreditLevel level;

	/**
	 * 借款金额
	 */
	public BigDecimal loanAmount = new BigDecimal(0);
	/**
	 * 年化利率
	 */
	public BigDecimal proportion = new BigDecimal(0);

	/**
	 * 期限
	 */
	public int day;
	/**
	 * 满标时间
	 */
	public Timestamp expireDatetime;
	/**
	 * 借款类型
	 */
	public InvestType type;

}
