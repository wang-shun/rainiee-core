package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6058_F12;
import com.dimeng.p2p.S60.enums.T6058_F18;

/**
 * 企业标意向表
 */
public class T6058 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	public int F01;

	/**
	 * 用户id，参考T6010.F01
	 */
	public int F02;

	/**
	 * 企业名称
	 */
	public String F03;

	/**
	 * 注册号
	 */
	public String F04;

	/**
	 * 法人
	 */
	public String F05;

	/**
	 * 身份证号
	 */
	public String F06;

	/**
	 * 联系电话
	 */
	public String F07;

	/**
	 * 所在城市编码
	 */
	public int F08;

	/**
	 * 借款金额
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 周期
	 */
	public int F10;

	/**
	 * 借款期限
	 */
	public int F11;

	/**
	 * 处理状态,WCL:未处理;YCL:已处理
	 */
	public T6058_F12 F12;

	/**
	 * 处理结果
	 */
	public String F13;

	/**
	 * 处理人,参考T7011.F01
	 */
	public int F14;

	/**
	 * 提交时间
	 */
	public Timestamp F15;

	/**
	 * 处理时间
	 */
	public Timestamp F16;

	/**
	 * 借款用途
	 */
	public String F17;

	/**
	 * 借款类型,QYXY:企业信用标;QYDYDB:企业抵押担保标
	 */
	public T6058_F18 F18;

	/**
	 * 公司地址
	 */
	public String F19;

	/**
	 * 联系人
	 */
	public String F20;

	/**
	 * 预计筹款期限
	 */
	public String F21;

}
