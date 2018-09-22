package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6042_F06;
import com.dimeng.p2p.S60.enums.T6042_F07;
import com.dimeng.p2p.S60.enums.T6042_F14;
import com.dimeng.p2p.S60.enums.T6042_F24;

/**
 * 优选理财计划表
 */
public class T6042 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 优选理财计划表自增ID
	 */
	public int F01;

	/**
	 * 标题
	 */
	public String F02;

	/**
	 * 实际金额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 余额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 预计收益率
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 投资范围,SDRZ:实地认证;JGDB:机构担保;SDRZJJGDB:实地认证+机构担保
	 */
	public T6042_F06 F06;

	/**
	 * 计划状态,XJ:新建;YFB:已发布;YSX:已生效;YJZ:已截止
	 */
	public T6042_F07 F07;

	/**
	 * 满额用时（秒计算）
	 */
	public int F08;

	/**
	 * 申请开始时间
	 */
	public Timestamp F09;

	/**
	 * 申请结束日期
	 */
	public Date F10;

	/**
	 * 锁定期限(月)
	 */
	public int F11;

	/**
	 * 满额时间
	 */
	public Timestamp F12;

	/**
	 * 锁定结束日期
	 */
	public Date F13;

	/**
	 * 收益处理,MYHXDQHB:每月还息，到期还本;DQBXYCXZF:到期本息一次性支付;DEBXMYHF:等额本息每月返还
	 */
	public T6042_F14 F14;

	/**
	 * 加入费率
	 */
	public BigDecimal F15 = BigDecimal.ZERO;

	/**
	 * 服务费率
	 */
	public BigDecimal F16 = BigDecimal.ZERO;

	/**
	 * 退出费率
	 */
	public BigDecimal F17 = BigDecimal.ZERO;

	/**
	 * 计划介绍
	 */
	public String F18;

	/**
	 * 创建人,参考T7011.F01
	 */
	public int F19;

	/**
	 * 创建时间
	 */
	public Timestamp F20;

	/**
	 * 下一还款日
	 */
	public Date F21;

	/**
	 * 投资下限
	 */
	public BigDecimal F22 = BigDecimal.ZERO;

	/**
	 * 投资上限
	 */
	public BigDecimal F23 = BigDecimal.ZERO;

	/**
	 * 保障方式,QEBXBZ:全额本息保障
	 */
	public T6042_F24 F24;

	/**
	 * 计划原始金额
	 */
	public BigDecimal F25 = BigDecimal.ZERO;

}
