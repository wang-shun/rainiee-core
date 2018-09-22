package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6110_F10;

public class FundsView extends T6101 {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户类型
	 */
	public String userType;

	/**
	 * 是否担保方
	 */
	public T6110_F10 isDbf;
	/**
	 * 用户受益
	 */
	public BigDecimal yhsy;

}
