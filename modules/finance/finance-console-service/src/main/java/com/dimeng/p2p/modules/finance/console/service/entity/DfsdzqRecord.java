package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.RestoreStatus;

public class DfsdzqRecord {

	/**
	 * 垫付时间
	 */
	public Timestamp paymentTime;
	/**
	 * 垫付金额
	 */
	public BigDecimal paymentAmount = new BigDecimal(0);
	/**
	 * 债权id
	 */
	public int id;
	/**
	 * 垫付返还金额
	 */
	public BigDecimal paymentRestore = new BigDecimal(0);

	/**
	 * 剩余期限
	 */
	public int residueDeadLine;
	/**
	 * 筹标期限
	 */
	public int day;

	/**
	 * 待收金额
	 */
	public BigDecimal restoreAmount = new BigDecimal(0);

	/**
	 * 下一个还款日
	 */
	public Timestamp nextRestoreTime;

	/**
	 * 还款状态
	 */
	public RestoreStatus status;
	/**
	 * 用户名
	 */
	public String loginName;

}
