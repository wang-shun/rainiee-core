package com.dimeng.p2p.repeater.policy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 贷款数据统计-按期限
 */
public class VolumeTimeLimit implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 笔数
	 */
	public int count;
	/**
	 * 金额
	 */
	public BigDecimal amount = new BigDecimal(0);
}
