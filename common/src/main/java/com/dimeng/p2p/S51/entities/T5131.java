package com.dimeng.p2p.S51.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5131_F02;

/**
 *平台垫付类型管理
 */
public class T5131 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 垫付类型
	 */
	public T5131_F02 F02;

}
