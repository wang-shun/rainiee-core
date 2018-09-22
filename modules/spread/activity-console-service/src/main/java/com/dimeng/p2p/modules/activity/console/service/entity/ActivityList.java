package com.dimeng.p2p.modules.activity.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
/**
 * 活动列表
 */
public class ActivityList {

	/**
	 * 主键id
	 */
	public int F01;
	
	/**
	 * 活动编码
	 */
	public String F02;
	
	/**
	 * 奖励类型
	 */
	public T6340_F03 F03;
	
	/**
	 * 活动类型
	 */
	public T6340_F04 F04;
	
	/**
	 * 活动名称
	 */
	public String F05;
	
	/**
	 * 活动开始日期
	 */
	public Timestamp F06;
	
	/**
	 * 活动结束日期
	 */
	public Timestamp F07;
	
	/**
	 * 发放数量
	 */
	public int F08;
	
	/**
	 * 使用有效期
	 */
	public int F09;
	
	/**
	 * 价值：红包单位为元，加息卷单位是%
	 */
	public BigDecimal F10 = BigDecimal.ZERO;
	
	/**
	 * 已领取数量
	 */
	public int F11;

	/**
	 * 活动状态
	 */
	public T6340_F08 F12;
	/**
	 * 修改时间
	 */
	public Timestamp F13;

	/**
	 * 已领取金额
	 */
	public BigDecimal F14 = BigDecimal.ZERO;
}
