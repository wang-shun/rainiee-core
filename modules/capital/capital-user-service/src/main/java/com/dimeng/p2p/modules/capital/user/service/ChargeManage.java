package com.dimeng.p2p.modules.capital.user.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.capital.user.service.entity.Auth;

/**
 * 充值
 */
public abstract interface ChargeManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 获取账户余额
	 * </dl>
	 * <dl>
	 * <ol>
	 * T6023.F03字段
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return 账户余额
	 * @throws Throwable
	 */
	public abstract BigDecimal balance() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 添加充值订单
	 * </dl>
	 * <dl>
	 * <ol>
	 * <li>判断用户是否登录</li>
	 * <li>插入表T6033，字段F02，F03，F04，F06</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param amount
	 *            充值金额
	 * @param payType
	 *            支付方式，通联或双乾
	 * @return
	 * 
	 * 
	 * 
	 * @throws Throwable
	 */
	public abstract int addOrder(BigDecimal amount, String payType)
			throws Throwable;

	/**
	 * 获取认证信息
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract Auth getAutnInfo() throws Throwable;
}
