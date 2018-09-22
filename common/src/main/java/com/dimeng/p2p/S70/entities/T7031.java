package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7031_F03;
import com.dimeng.p2p.S70.enums.T7031_F12;

/**
 * 合同信息表
 */
public class T7031 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 合同信息表自增ID
	 */
	public int F01;

	/**
	 * 机构信息表ID,参考T7029.F01
	 */
	public int F02;

	/**
	 * 合同类型,SDRZ:实地认证;DYDB:机构担保
	 */
	public T7031_F03 F03;

	/**
	 * 合同号
	 */
	public String F04;

	/**
	 * 借款者姓名
	 */
	public String F05;

	/**
	 * 借款者身份证
	 */
	public String F06;

	/**
	 * 机构服务费率
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 合同金额
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 合同余额
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 创建人,参考T7011.F01
	 */
	public int F10;

	/**
	 * 创建时间
	 */
	public Timestamp F11;

	/**
	 * 合同状态,YX:有效;WX无效
	 */
	public T7031_F12 F12;

	/**
	 * 用户ID,参考T6010.F01
	 */
	public int F13;

}
