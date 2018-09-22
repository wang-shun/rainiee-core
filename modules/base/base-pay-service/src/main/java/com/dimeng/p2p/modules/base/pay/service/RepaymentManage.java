package com.dimeng.p2p.modules.base.pay.service;

import com.dimeng.framework.service.Service;

/**
 * 还款管理
 */
public interface RepaymentManage extends Service {

    /**
    * 系统自动还款
    * 
    * @param bidId
    *            标ID
    * @param term
    *            期号
    * @return 订单ID列表
    * @throws Throwable
    */
	public int[] repayment() throws Throwable;
}
