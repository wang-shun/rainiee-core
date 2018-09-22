package com.dimeng.p2p.modules.base.front.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 理财信息 
 *
 */
public class LcInfo {
	/**
	 * 用户名
	 */
	public String userName;
	/**
	 * 理财金额
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 投资时间
	 */
	public Timestamp time;
}
