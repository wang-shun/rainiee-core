package com.dimeng.p2p.S60.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6016_F02;

/**
 * 用户其他信息
 */
public class T6016 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户帐号ID
	 */
	public int F01;

	/**
	 * 是否微博认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6016_F02 F02;

}
