package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6230_F20;

/**
 * 投资记录
 * 
 */
public class TzjlEntity extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	/**
	 * 标id
	 */
	public int id;
	/**
	 * 投资项目
	 */
	public String name;
	/**
	 * 投资金额
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 收益率
	 */
	 public BigDecimal rate = new BigDecimal(0);
	/**
	 * 投资时间
	 */
	public Timestamp startTime;
	/**
	 * 到期日
	 */
	public Timestamp endTime;
	/**
	 * 状态
	 */
	public T6230_F20 type;
	/**
	 * 投资开始时间（查询条件）
	 */
	public Timestamp tjStartTime;
	/**
	 * 投资结束时间（查询条件）
	 */
	public Timestamp tjEndTime;
	
}
