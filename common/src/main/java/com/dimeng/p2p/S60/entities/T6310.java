package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 业务员信息表
 */
public class T6310 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 业务员编号
	 */
	public int F01;

	/**
	 * 姓名
	 */
	public String F02;

	/**
	 * 手机号码
	 */
	public String F03;

	/**
	 * 新增时间
	 */
	public Timestamp F04;

	/**
	 * 最后更新时间
	 */
	public Timestamp F05;

}
