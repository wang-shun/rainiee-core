package com.dimeng.p2p.modules.financing.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ZqzrInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 债权价值
	 */
	public BigDecimal zqjz = new BigDecimal(0);
	/**
	 * 转让价款
	 */
	public BigDecimal zrjk = new BigDecimal(0);
	/**
	 * 转让手续费
	 */
	public BigDecimal zrglf = new BigDecimal(0);
	/**
	 * 转让时间
	 */
	public Timestamp zrsj;
	/**
	 * 剩余期数
	 */
	public int syqs;
}
