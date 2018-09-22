package com.dimeng.p2p.S60.entities;

import java.sql.Blob;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 标的附件内容表
 */
public class T6217 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 标的附件表ID(参考T6216.F01)
	 */
	public int F01;

	/**
	 * 附件内容
	 */
	public Blob F02;

}
