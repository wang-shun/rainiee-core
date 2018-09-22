package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6035_F06;

/**
 * 站内消息
 */
public class T6035 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 站内消息自增ID
	 */
	public int F01;

	/**
	 * 发送时间
	 */
	public Timestamp F02;

	/**
	 * 标题
	 */
	public String F03;

	/**
	 * 备用字段
	 */
	public String F04;

	/**
	 * 接收人,参考T6010.F01
	 */
	public int F05;

	/**
	 * 状态,WD:未读;YD:已读;SC:删除
	 */
	public T6035_F06 F06;

}
