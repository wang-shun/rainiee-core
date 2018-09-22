package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 成交数据统计
 */
public class VolumeEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 月份
	 */
	public int month;
	/**
	 * 成交金额
	 */
	public BigDecimal amount = new BigDecimal(0);
	/**
	 * 成交笔数
	 */
	public int count;
}
