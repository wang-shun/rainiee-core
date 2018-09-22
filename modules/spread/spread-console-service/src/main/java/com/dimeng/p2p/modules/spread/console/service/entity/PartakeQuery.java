package com.dimeng.p2p.modules.spread.console.service.entity;

import java.sql.Timestamp;
/**
 * 参与人列表的查询条件
 */
public interface PartakeQuery {
	/**
	 * 参与人
	 */
	public String userName();
	/**
	 * 开始时间
	 */
	public Timestamp startTime();
	/**
	 * 结束时间
	 */
	public Timestamp endTime();
	
}
