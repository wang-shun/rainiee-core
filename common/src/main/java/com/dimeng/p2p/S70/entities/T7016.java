package com.dimeng.p2p.S70.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 站内信推广指定人列表
 */
public class T7016 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 站内信推广指定人列表自增ID
	 */
	public int F01;

	/**
	 * 站内信推广ID,参考T7015.F01
	 */
	public int F02;

	/**
	 * 指定人账户名
	 */
	public String F03;

}
