package com.dimeng.p2p.modules.finance.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 加入用户
 */
public class JoinedUser implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 用户名
	 */
	public String userName;
	/**
	 * 借出金额
	 */
	public BigDecimal amount = new BigDecimal(0);
	/**
	 * 借款期限
	 */
	public int limitMonth;
	/**
	 * 月还金额
	 */
	public BigDecimal monthAmount = new BigDecimal(0);
}
