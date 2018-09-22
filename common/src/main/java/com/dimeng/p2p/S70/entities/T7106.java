package com.dimeng.p2p.S70.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 企业与合同关系表
 */
public class T7106 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 合同id，参考T70
	 */
	public int F01;

	/**
	 * 企业编号，参考T6110.F07
	 */
	public String F02;

}
