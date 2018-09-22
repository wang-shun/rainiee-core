package com.dimeng.p2p.S60.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6010_F06;
import com.dimeng.p2p.S60.enums.T6010_F07;

/**
 * 用户账号
 */
public class T6010 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号自增ID
	 */
	public int F01;

	/**
	 * 账号
	 */
	public String F02;

	/**
	 * 密码
	 */
	public String F03;

	/**
	 * 手机号
	 */
	public String F04;

	/**
	 * 邮箱
	 */
	public String F05;

	/**
	 * 状态,ZC正常;SD:锁定
	 */
	public T6010_F06 F06;

	/**
	 * 注册来源,ZC:注册;HT:后台;
	 */
	public T6010_F07 F07;

	/**
	 * 锁定原因
	 */
	public String F08;

	/**
	 * 当日交易密码输入错误次数
	 */
	public short F09;

}
