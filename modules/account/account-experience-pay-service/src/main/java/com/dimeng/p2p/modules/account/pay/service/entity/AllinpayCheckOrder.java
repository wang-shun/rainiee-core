package com.dimeng.p2p.modules.account.pay.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.ChargeStatus;

public class AllinpayCheckOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 订单id
	 */
	public int id;
	/**
	 * 订单创建时间
	 */
	public Timestamp createTime;
	/**
	 * 订单号
	 */
	public String orderNo;
	/**
	 * 订单支付状态
	 */
	public ChargeStatus chargeStatus;

}
