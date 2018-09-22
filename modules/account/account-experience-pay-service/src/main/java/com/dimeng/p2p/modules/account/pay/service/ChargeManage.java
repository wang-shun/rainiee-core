package com.dimeng.p2p.modules.account.pay.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.account.pay.service.entity.AllinpayCheckOrder;
import com.dimeng.p2p.modules.account.pay.service.entity.Auth;
import com.dimeng.p2p.modules.account.pay.service.entity.ChargeOrder;

public abstract interface ChargeManage extends Service {

	/**
	 * 获取订单的创建时间
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract AllinpayCheckOrder getAllinpayCheckOrder(int id)
			throws Throwable;

	/**
	 * 添加充值订单
	 * 
	 * @param amount
	 *            充值金额
	 * @param payCompanyCode
	 *            支付公司代号
	 * @return
	 * 
	 * @throws Throwable
	 */
	public abstract ChargeOrder addOrder(BigDecimal amount, int payCompanyCode)
			throws Throwable;

	/**
	 * 获取认证信息
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract Auth getAutnInfo() throws Throwable;
	/**
	 * 获取订单
	 * @param orderId
	 * @return
	 * @throws Throwable
	 */
	public abstract ChargeOrder getOrder(int orderId) throws Throwable;
	/**
	 * 后台获取充值订单
	 * @param orderId
	 * @return
	 * @throws Throwable
	 */
	public abstract ChargeOrder getChargeOrder(int orderId) throws Throwable;
	/**
	 * 更改订单未待确认状态
	 * @param orderId
	 * @throws Throwable
	 */
	public abstract void updateOrder(int orderId) throws Throwable;
	
	ChargeOrder getLockChargeOrder(int orderId) throws Throwable;
}
