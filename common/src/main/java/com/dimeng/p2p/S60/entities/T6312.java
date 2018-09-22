package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 购买标的信息记录表
 */
public class T6312 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	public int F01;

	/**
	 * 购买用户ID（T6010.F01）
	 */
	public int F02;

	/**
	 * 标的ID（T6036.F01）
	 */
	public int F03;

	/**
	 * 购买时间
	 */
	public Timestamp F04;

	/**
	 * 购买服务费
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

}
