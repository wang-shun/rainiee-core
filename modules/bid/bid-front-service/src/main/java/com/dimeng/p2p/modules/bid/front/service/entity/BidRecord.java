package com.dimeng.p2p.modules.bid.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 投资记录
 */
public class BidRecord implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 账号名(显示3位，其他用*代替)
	 */
	public String accountName;
	/**
	 * 投资金额
	 */
	public BigDecimal bidAmount;
	
	/**
	 * 投资时间
	 */
	public Timestamp bidTime;
	
	/**
	 * 省份
	 */
	public String province;
}
