package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 法人基础信息表
 */
public class T6210 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID(参考 T6010.F01)
	 */
	public int F01;

	/**
	 * 企业纳税号
	 */
	public String F02;

	/**
	 * 企业名称
	 */
	public String F03;

	/**
	 * 企业地址
	 */
	public String F04;

	/**
	 * 联系人
	 */
	public String F05;

	/**
	 * 联系电话
	 */
	public String F06;

	/**
	 * 企业营业执照登记注册号
	 */
	public String F07;

	/**
	 * 组织机构号
	 */
	public String F08;

	/**
	 * 邮箱地址
	 */
	public String F09;

	/**
	 * 注册时间
	 */
	public Timestamp F10;

}
