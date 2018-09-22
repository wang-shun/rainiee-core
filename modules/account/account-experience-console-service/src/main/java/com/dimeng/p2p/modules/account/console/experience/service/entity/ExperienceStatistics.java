package com.dimeng.p2p.modules.account.console.experience.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class ExperienceStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 标ID
	 */
	public int bidId;
	/**
	 * 用户名
	 */
	public String accountName;
	/**
	 * 姓名
	 */
	public String name;
	/**
	 * 标编号
	 */
	public String bidNo;
	/**
	 * 本期应还
	 */
	public BigDecimal amount;
	/**
	 * 体验金
	 */
	public BigDecimal experience;
	/**
	 * 下一还款日
	 */
	public Date time;
	/**
	 * 距下次还款日
	 */
	public int day;
	/**
	 * 实际返还日期
	 */
	public Timestamp fhTime;
}
