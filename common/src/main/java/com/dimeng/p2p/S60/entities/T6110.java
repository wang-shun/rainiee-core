package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 企业账户信息
 */
public class T6110 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	public int F01;

	/**
	 * 企业名称
	 */
	public String F02;

	/**
	 * 注册号
	 */
	public String F03;

	/**
	 * 法人
	 */
	public String F04;

	/**
	 * 法人身份证号
	 */
	public String F05;

	/**
	 * 所在城市编码
	 */
	public int F06;

	/**
	 * 企业编号
	 */
	public String F07;

	/**
	 * 联系电话
	 */
	public String F08;

	/**
	 * 邮箱
	 */
	public String F09;

	/**
	 * 创建时间
	 */
	public Timestamp F10;

	/**
	 * 最后更新时间
	 */
	public Timestamp F11;

}
