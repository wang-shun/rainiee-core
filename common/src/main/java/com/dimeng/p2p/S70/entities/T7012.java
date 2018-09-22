package com.dimeng.p2p.S70.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7012_F05;

/**
 * 后台登录日志
 */
public class T7012 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 后台登录日志自增ID
	 */
	public int F01;

	/**
	 * 后台账号
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
	 * 是否成功登录,CG:成功;SB:失败
	 */
	public T7012_F05 F05;

}
