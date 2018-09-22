package com.dimeng.p2p.S60.enums;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 后台操作日志表
 */
public class T7201 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	public int F01;

	/**
	 * 后台账号(关联T7011.F01)
	 */
	public int F02;

	/**
	 * 关联ID
	 */
	public int F03;

	/**
	 * 操作时间
	 */
	public Timestamp F04;

	/**
	 * 操作描述
	 */
	public String F05;

	/**
	 * 角色ID
	 */
	public int F06;

	/**
	 * 访问IP
	 */
	public String F07;

}
