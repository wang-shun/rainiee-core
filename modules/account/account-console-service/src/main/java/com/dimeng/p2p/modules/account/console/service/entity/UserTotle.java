package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S61.enums.T6110_F06;

public class UserTotle {
	/**
	 * 用户ID
	 */
	public int userId;
	/**
	 * 用户名
	 */
	public String loginName;
	/**
	 * 投资总计
	 */
	public BigDecimal tzTotle= new BigDecimal(0);
	/**
	 * 收益总计
	 */
	public BigDecimal syTotle= new BigDecimal(0);
	/**
	 * 用户类型
	 */
	public T6110_F06 userType;
}
