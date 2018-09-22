package com.dimeng.p2p.S50.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 广告管理
 */
public class T5016_1 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 广告管理自增ID
	 */
	public int F01;

	/**
	 * 排序值
	 */
	public int F02;

	/**
	 * 广告图片标题
	 */
	public String F03;

	/**
	 * 图片链接
	 */
	public String F04;

	/**
	 * 图片编码
	 */
	public String F05;

	/**
	 * 创建者,参考T7011.F01
	 */
	public int F06;

	/**
	 * 上架时间
	 */
	public Timestamp F07;

	/**
	 * 下架时间
	 */
	public Timestamp F08;

	/**
	 * 创建时间
	 */
	public Timestamp F09;

	/**
	 * 最后修改时间
	 */
	public Timestamp F10;
	
	/**
	 * 广告内容
	 */
	public String F11;

}
