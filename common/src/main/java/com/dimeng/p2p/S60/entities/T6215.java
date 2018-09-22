package com.dimeng.p2p.S60.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 标的担保（风控）信息表
 */
public class T6215 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 借款标ID
	 */
	public int F01;

	/**
	 * 机构ID(参考T7029.F01)
	 */
	public int F02;

	/**
	 * 担保情况
	 */
	public String F03;

	/**
	 * 反担保情况
	 */
	public String F04;

	/**
	 * 风控综合描述
	 */
	public String F05;

}
