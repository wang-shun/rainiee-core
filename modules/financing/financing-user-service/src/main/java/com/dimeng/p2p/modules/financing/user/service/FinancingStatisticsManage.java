package com.dimeng.p2p.modules.financing.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.financing.user.service.entity.AccountBalance;
import com.dimeng.p2p.modules.financing.user.service.entity.EarnFinancingTotal;
import com.dimeng.p2p.modules.financing.user.service.entity.FinancingTotal;
import com.dimeng.p2p.modules.financing.user.service.entity.UnpayEarnings;

/**
 * 理财统计借款
 * 
 */
public abstract interface FinancingStatisticsManage extends Service {
	/**
	 * 得到平台已赚取的信息
	 * 
	 * @return
	 */
	public EarnFinancingTotal getEarnFinancingTotal() throws Throwable;

	/**
	 * 得到平台总计投资信息
	 * 
	 * @return
	 */
	public FinancingTotal getFinancingTotal() throws Throwable;

	/**
	 * 获取理财账户资产
	 * 
	 * @return
	 * @throws Throwable
	 */
	public AccountBalance getAccountBalance() throws Throwable;

	/**
	 * 获取债权待收收益
	 * 
	 * @return
	 * @throws Throwable
	 */
	public UnpayEarnings getUnpayEarnings() throws Throwable;
}
