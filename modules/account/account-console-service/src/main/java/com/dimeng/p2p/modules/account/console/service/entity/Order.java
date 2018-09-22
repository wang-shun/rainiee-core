/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S65.entities.T6501;

/**
 * @author guopeng
 * 
 */
public class Order extends T6501 {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	public String userName;
	/**
	 * 后台账号
	 */
	public String name;
	/**
	 * 金额
	 */
	public BigDecimal amount = new BigDecimal(0);

	/**
	 * 不良债权ID
	 */
	public int transferId;

	/**
	 * 冻结订单号
	 */
	public String freezeOrdId;

}
