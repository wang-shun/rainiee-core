package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.InvestType;
/**
 *	贷款数据统计-按产品类型
 */
public class VolumeType implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 产品类型
	 */
	public InvestType type;
	/**
	 * 笔数
	 */
	public int count;
	/**
	 * 金额
	 */
	public BigDecimal amount = new BigDecimal(0);
}
