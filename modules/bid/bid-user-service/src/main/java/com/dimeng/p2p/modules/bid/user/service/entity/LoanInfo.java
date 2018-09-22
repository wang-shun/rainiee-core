package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LoanInfo {
	/**
	 * 标ID
	 */
	public int loanId;
	/**
	 * 标题
	 */
	public String loanTitle;
	/**
	 * 电话
	 */
	public String tel;
	/**
	 * 借款金额
	 */
	public BigDecimal jkje = new BigDecimal(0);
	/**
	 * 年率
	 */
	public double rate;
	/**
	 * 借款期限
	 */
	public int jkqx;
	/**
	 * 借款用户ID
	 */
	public int jkUserId;
	/**
	 * 查看费用
	 */
	public BigDecimal ckfy;
	/**
	 * 提交时间
	 */
	public Timestamp tjsj;

}
