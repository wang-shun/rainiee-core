package com.dimeng.p2p.S70.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7011_F05;
import com.dimeng.p2p.S70.enums.T7011_F09;

/**
 * 后台账号表
 */
public class T7011 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 后台账号表自增ID
	 */
	public int F01;

	/**
	 * 登录名
	 */
	public String F02;

	/**
	 * 密码
	 */
	public String F03;

	/**
	 * 真实姓名
	 */
	public String F04;

	/**
	 * 状态,QY:启用;TY:停用
	 */
	public T7011_F05 F05;

	/**
	 * 创建时间
	 */
	public Timestamp F06;

	/**
	 * 最后登录时间
	 */
	public Timestamp F07;

	/**
	 * 登录IP
	 */
	public String F08;

	/**
	 * 是否第一次登录修改密码,S:是;F:否
	 */
	public T7011_F09 F09;

}
