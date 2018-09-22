package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * 优选理财加入记录
 */
public class YxJoined implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 加入时间
	 */
	public Timestamp time;
	/**
	 * 加入金额
	 */
	public BigDecimal amount = new BigDecimal(0);
}
