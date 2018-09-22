package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class YxDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 申请开始时间
	 */
	public Timestamp sqks;
	/**
	 * 申请结束时间
	 */
	public Timestamp sqjs;
	/**
	 * 满额时间
	 */
	public Timestamp mesj;
	/**
	 * 理财锁定结束时间
	 */
	public Timestamp lcjs;
	/**
	 * 优选理财标题名
	 */
	public String title;
	/**
	 * 加入上限
	 */
	public BigDecimal upper = new BigDecimal(0);
	/**
	 * 加入下限
	 */
	public BigDecimal low = new BigDecimal(0);
	/**
	 * 加入费率
	 */
	public BigDecimal jrfl = new BigDecimal(0);
	/**
	 * 服务费率
	 */
	public BigDecimal fwfl = new BigDecimal(0);
	/**
	 * 退出费率
	 */
	public BigDecimal tcfl = new BigDecimal(0);
}
