package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7029_F03;
import com.dimeng.p2p.S70.enums.T7029_F05;

/**
 * 机构信息表
 */
public class T7029 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 机构信息表自增ID
	 */
	public int F01;

	/**
	 * 名称
	 */
	public String F02;

	/**
	 * 类型,SDRZ:实地认证;DYDB:机构担保;SDRZJDYDB:实地认证+机构担保
	 */
	public T7029_F03 F03;

	/**
	 * 联系地址
	 */
	public String F04;

	/**
	 * 状态,YX:有效;WX:无效;SC:删除
	 */
	public T7029_F05 F05;

	/**
	 * 创建人,参考T7011.F01
	 */
	public int F06;

	/**
	 * 创建时间
	 */
	public Timestamp F07;

	/**
	 * 最后修改时间
	 */
	public Timestamp F08;

	/**
	 * 机构信用额度
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 机构可用信用额度
	 */
	public BigDecimal F10 = BigDecimal.ZERO;

	/**
	 * 原始备用金总额
	 */
	public BigDecimal F11 = BigDecimal.ZERO;

	/**
	 * 备用金余额
	 */
	public BigDecimal F12 = BigDecimal.ZERO;

	/**
	 * 机构标识符,唯一
	 */
	public String F13;

}
