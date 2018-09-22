package com.dimeng.p2p.S70.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 修改系统常量日志表
 */
public class T7102 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * KEY值
	 */
	public String F02;

	/**
	 * 常量名称
	 */
	public String F03;

	/**
	 * 修改前值
	 */
	public String F04;

	/**
	 * 修改后值
	 */
	public String F05;

	/**
	 * 修改人ID,参考T7011表ID
	 */
	public int F06;

	/**
	 * 修改时间
	 */
	public Timestamp F07;

}
