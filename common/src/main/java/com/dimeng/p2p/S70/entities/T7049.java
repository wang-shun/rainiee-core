package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7049_F05;

/**
 * 线下充值记录表
 */
public class T7049 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 线下充值记录表自增ID
	 */
	public int F01;

	/**
	 * 用户ID,参考T6010.F01
	 */
	public int F02;

	/**
	 * 时间
	 */
	public Timestamp F03;

	/**
	 * 创建人,参考T7011.F01
	 */
	public int F04;

	/**
	 * 充值状态，DSH：待审核；YCZ：已充值；YQX：已取消
	 */
	public T7049_F05 F05;

	/**
	 * 金额
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 备注
	 */
	public String F07;

	/**
	 * 审核人ID,参考T7011.F01
	 */
	public int F08;

	/**
	 * 审核时间
	 */
	public Timestamp F09;

	/**
	 * 审核意见
	 */
	public String F10;

}
