package com.dimeng.p2p.modules.base.pay.service;

import java.util.List;

import com.dimeng.framework.service.Service;

/**
 * 罚息计算
 */
public abstract interface DefaultInterestManage extends Service {

	/**
	 * 罚息计算.
	 * 
	 * @throws Throwable
	 */
	public abstract void calculate() throws Throwable;

	/**
	 * 自动投资
	 */
	public abstract List<Integer> autoBid() throws Throwable;
}
