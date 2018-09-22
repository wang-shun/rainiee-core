package com.dimeng.p2p.S50.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 友情链接
 */
public class T5014 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 友情链接自增ID
	 */
	public int F01;

	/**
	 * 浏览次数
	 */
	public int F02;

	/**
	 * 排序值
	 */
	public int F03;

	/**
	 * 名称
	 */
	public String F04;

	/**
	 * 链接地址
	 */
	public String F05;

	/**
	 * 创建者,参考T7011.F01
	 */
	public int F06;

	/**
	 * 创建时间
	 */
	public Timestamp F07;

	/**
	 * 最后更新时间
	 */
	public Timestamp F08;

}
