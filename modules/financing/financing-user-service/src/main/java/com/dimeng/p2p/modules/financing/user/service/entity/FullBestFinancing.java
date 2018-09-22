package com.dimeng.p2p.modules.financing.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class FullBestFinancing implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2966387028016583953L;

	/**
	 * 持有人用户ID
	 */
	public int cyrId;
	/**
	 * 加入金额
	 */
	public BigDecimal joinMoney;
}
