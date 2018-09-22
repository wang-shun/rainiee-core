package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 贷款数据统计-按地域
 */
public class VolumeRegion implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 地域id
	 */
	public int regionId;
	/**
	 * 省
	 */
	public String province;
	/**
	 * 笔数
	 */
	public int count;
	/**
	 * 金额
	 */
	public BigDecimal amount = new BigDecimal(0);
}