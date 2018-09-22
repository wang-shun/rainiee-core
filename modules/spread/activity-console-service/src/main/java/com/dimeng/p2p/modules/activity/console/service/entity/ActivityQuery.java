package com.dimeng.p2p.modules.activity.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
/**
 * 活动列表查询条件 
 *
 */
public interface ActivityQuery {
	/**
	 * 活动编码
	 */
	public String code();
	/**
	 * 活动名称
	 * @return
	 */
	public String name();
	/**
	 * 奖励类型
	 * @return
	 */
	public T6340_F03 jlType();
	/**
	 * 活动类型
	 * @return
	 */
	public T6340_F04 hdType();
	/**
	 * 开始时间(start)
	 */
	public Timestamp startsTime();
	/**
	 * 开始时间(end)
	 */
	public Timestamp starteTime();
	/**
	 * 结束时间(start)
	 */
	public Timestamp endsTime();
	/**
	 * 结束时间(end)
	 */
	public Timestamp endeTime();
	/**
	 * 状态
	 */
	public T6340_F08 status();
}
