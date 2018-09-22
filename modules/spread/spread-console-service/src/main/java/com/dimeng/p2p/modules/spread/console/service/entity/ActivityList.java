package com.dimeng.p2p.modules.spread.console.service.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.p2p.S63.enums.T6320_F09;
import com.dimeng.p2p.S63.enums.T6320_F10;
import com.dimeng.p2p.S63.enums.T6320_F12;
import com.dimeng.p2p.S63.enums.T6320_F14;

/**
 * 活动列表
 */
public class ActivityList {

	/**
	 * 自增id，无符号，主键
	 */
	public int F01;

	/**
	 * 活动名称
	 */
	public String F02;

	/**
	 * 活动开始时间
	 */
	public Timestamp F03;

	/**
	 * 活动结束时间
	 */
	public Timestamp F04;

	/**
	 * 优惠券生效日期
	 */
	public Date F05;

	/**
	 * 优惠券失效日期
	 */
	public Date F06;

	/**
	 * 发放数量
	 */
	public int F07;

	/**
	 * 金额
	 */
	public BigDecimal F08 = new BigDecimal(0);

	/**
	 * 优惠券获取方式，CZ:充值;TG:推广;YHLQ:用户领取;ZJFF:直接发放;
	 */
	public T6320_F09 F09;

	/**
	 * 是否充值可用，S:是，F：否
	 */
	public T6320_F10 F10;

	/**
	 * 使用时最低充值金额
	 */
	public BigDecimal F11 = new BigDecimal(0);

	/**
	 * 是否投资可用,S:是,F:否
	 */
	public T6320_F12 F12;

	/**
	 * 使用时最低投资金额
	 */
	public BigDecimal F13 = new BigDecimal(0);

	/**
	 * 状态,XJ:新建,SX:生效,YJS:已结束
	 */
	public T6320_F14 F14;

	/**
	 * 最低充值金额
	 */
	public BigDecimal F15 = new BigDecimal(0);

	/**
	 * 最低推广人数
	 */
	public int F16;

	/**
	 * 创建人id,参考T7110.F01
	 */
	public String F17;

	/**
	 * 剩余可发放数量
	 */
	public int F18;

}
