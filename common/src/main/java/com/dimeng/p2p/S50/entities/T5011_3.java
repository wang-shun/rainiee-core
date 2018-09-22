package com.dimeng.p2p.S50.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 
 * <一句话功能简述> <功能详细描述>
 * 
 * @author God
 * @version [版本号, 2018年2月5日]
 */
public class T5011_3 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 文章自增ID
	 */
	public int F01;

	/**
	 * 文章ID,参考T5011.F01
	 */
	public int F02;

	/**
	 * 附件路径
	 */
	public String F03;

	/**
	 * 附件大小
	 */
	public String F04;

	/**
	 * 附件名称
	 */
	public String F05;

	/**
	 * 年份
	 */
	public int F06;

}
