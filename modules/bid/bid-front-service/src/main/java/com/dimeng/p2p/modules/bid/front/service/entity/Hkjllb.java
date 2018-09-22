package com.dimeng.p2p.modules.bid.front.service.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6252_F09;

/**
 * 还款记录列表
 * 
 */
public class Hkjllb extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2078821629327811993L;
	/**
	 * 金额
	 */
	public BigDecimal F01 = new BigDecimal(0);

	/**
	 * 应还日期
	 */
	public Date F02;

	/**
	 * 状态,WH:未还;YH:已还;
	 */
	public T6252_F09 F03;

	/**
	 * 实际还款时间
	 */
	public Timestamp F04;

	/**
	 * 类型名称
	 */
	public String F05;
	/**
	 * 未还金额
	 */
	public BigDecimal whAmount;

}
