package com.dimeng.p2p.modules.bid.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Tzr implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 自增id
	 */
	public int id;

	/**
	 * 投资人
	 */
	public int userId;
	/**
	 * 投资金额
	 */
	public BigDecimal amount = new BigDecimal(0);
	/**
	 * 借款标题
	 */
	public String title;

}
