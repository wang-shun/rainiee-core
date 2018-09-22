package com.dimeng.p2p.modules.finance.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class JgbyjStatistics implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 原始备用金总额
	 */
	public BigDecimal ysbyjAmount=new BigDecimal(0);

	/**
	 * 备用金总额
	 */
	public BigDecimal byjAmount=new BigDecimal(0);

	/**
	 * 信用额度总额
	 */
	public BigDecimal xyAmount=new BigDecimal(0);

	/**
	 * 可用额度总额
	 */
	public BigDecimal kyAmount=new BigDecimal(0);

}
