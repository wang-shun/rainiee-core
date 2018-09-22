package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6051_F05;

/**
 * 前台登录日志
 */
public class T6051 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 前台登录日志自增ID
	 */
	public int F01;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public String F02;

	/**
	 * 登录时间
	 */
	public Timestamp F03;

	/**
	 * 登录IP
	 */
	public String F04;

	/**
	 * 是否成功登录,CG:成功;SB失败
	 */
	public T6051_F05 F05;

}
