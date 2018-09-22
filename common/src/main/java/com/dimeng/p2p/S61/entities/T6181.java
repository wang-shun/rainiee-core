package com.dimeng.p2p.S61.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 担保方资料
 */
public class T6181 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID,参考T6110.F01
	 */
	public int F01;

	/**
	 * 担保情况
	 */
	public String F02;

	/**
	 * 反担保情况
	 */
	public String F03;

	/**
	 * 风控综合描述
	 */
	public String F04;

}
