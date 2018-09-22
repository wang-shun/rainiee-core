package com.dimeng.p2p.modules.spread.console.service.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *活动信息
 */
public interface Activity {
	/**
	 * 活动主题
	 */
	public String title();
	/**
	 * 金额
	 */
	public BigDecimal money();
	/**
	 * 开始时间
	 */
	public Timestamp startTime();
	/**
	 * 结束时间
	 */
	public Timestamp endTime();
	
	/**
	 * 优惠券生效时间
	 */
	public Date couponStartTime();
	
	/**
	 * 优惠券失效时间
	 */
	public Date couponEndTime();
	/**
	 *发放数量
	 */
	public int amount();
	/**
	 * 最低充值金额
	 */
	public BigDecimal leastRecharge();
	/**
	 * 最低投资金额
	 */
	public BigDecimal leastInvest();
	/**
	 * 推广人数 
	 */
	public int spreadPersons();
	/**
	 * 发布人
	 */
	public String publisher();
	/**
	 * 剩余数量
	 */
	public int remain();
}
