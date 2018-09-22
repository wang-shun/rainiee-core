package com.dimeng.p2p.S50.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 合作伙伴
 */
public class T5013 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 合作伙伴自增ID
	 */
	public int F01;

	/**
	 * 排序值
	 */
	public int F02;

	/**
	 * 浏览次数
	 */
	public int F03;

	/**
	 * 公司名称
	 */
	public String F04;

	/**
	 * 链接地址
	 */
	public String F05;

	/**
	 * 图片编码
	 */
	public String F06;

	/**
	 * 联系地址
	 */
	public String F07;

	/**
	 * 公司描述
	 */
	public String F08;

	/**
	 * 创建者ID,参考T7011.F01
	 */
	public int F09;

	/**
	 * 创建时间
	 */
	public Timestamp F10;

	/**
	 * 最后更新时间
	 */
	public Timestamp F11;

}
