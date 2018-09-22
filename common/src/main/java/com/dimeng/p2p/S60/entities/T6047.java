package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6047_F06;

/**
 * 邮件
 */
public class T6047 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 邮件自增ID
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
	 * 模板Key
	 */
	public String F04;

	/**
	 * 接受人,参考T6010.F01
	 */
	public int F05;

	/**
	 * 状态,WD:未读;YD:已读;SC:删除
	 */
	public T6047_F06 F06;

}
