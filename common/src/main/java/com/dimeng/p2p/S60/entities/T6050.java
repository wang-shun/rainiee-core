package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6050_F05;
import com.dimeng.p2p.S60.enums.T6050_F09;

/**
 * 借款意向表
 */
public class T6050 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 借款意向表自增ID
	 */
	public int F01;

	/**
	 * 联系人
	 */
	public String F02;

	/**
	 * 手机
	 */
	public String F03;

	/**
	 * 借款金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 借款类型,SDRZ:实地认证;JGDB机构担保;GRXY:个人信用，GRDYDB:个人抵押担保）
	 */
	public T6050_F05 F05;

	/**
	 * 所在城市
	 */
	public String F06;

	/**
	 * 预计筹款期限
	 */
	public String F07;

	/**
	 * 借款描述
	 */
	public String F08;

	/**
	 * 处理状态,WCL:未处理;YCL:已处理
	 */
	public T6050_F09 F09;

	/**
	 * 处理结果
	 */
	public String F10;

	/**
	 * 处理人,参考T7011.F01
	 */
	public int F11;

	/**
	 * 提交时间
	 */
	public Timestamp F12;

	/**
	 * 处理时间
	 */
	public Timestamp F13;

}
