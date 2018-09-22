package com.dimeng.p2p.modules.financial.console.service.entity;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6260;

/**
 * 待转让的债权
 *
 */
public class TransferDsh  extends T6260{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4380750453694680328L;
	/**
	 * 获取债权信息
	 */
	public T6251 t6251;
	/**
	 * 用户信息
	 */
	public T6110 t6110;
}
