package com.dimeng.p2p.S70.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 平台用户数据统计-按月
 */
public class T7035 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 年份
	 */
	public int F01;

	/**
	 * 月份
	 */
	public short F02;

	/**
	 * 用户数
	 */
	public int F03;

	/**
	 * 新增用户数
	 */
	public int F04;

	/**
	 * 借款用户数
	 */
	public int F05;

	/**
	 * 投资用户数
	 */
	public int F06;

	/**
	 * 更新时间
	 */
	public Timestamp F07;

	/**
	 * 新增借款人
	 */
	public int F08;

	/**
	 * 新增投资人
	 */
	public int F09;

}
