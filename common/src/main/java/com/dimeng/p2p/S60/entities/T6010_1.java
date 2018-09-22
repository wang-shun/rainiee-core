package com.dimeng.p2p.S60.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6010_1_F02;
import com.dimeng.p2p.S60.enums.T6010_1_F03;

/**
 * 用户关联类型表
 */
public class T6010_1 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID(参考 T6010.F01)
	 */
	public int F01;

	/**
	 * 法人和自然人
	 */
	public T6010_1_F02 F02;

	/**
	 * 是否第一次登录修改密码,S:是;F:否
	 */
	public T6010_1_F03 F03;

}
