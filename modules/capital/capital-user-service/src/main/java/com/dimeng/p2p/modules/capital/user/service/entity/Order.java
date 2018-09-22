package com.dimeng.p2p.modules.capital.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.ChargeStatus;

public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 订单id
	 */
	public int id;
	/**
	 * 金额
	 */
	public BigDecimal amount;
	/**
	 * 下单时间
	 */
	public Timestamp orderTime;
	/**
	 * 支付状态
	 */
	public ChargeStatus status;
}
