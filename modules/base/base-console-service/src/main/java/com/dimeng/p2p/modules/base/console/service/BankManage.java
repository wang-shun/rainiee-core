package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5020;
import com.dimeng.p2p.modules.base.console.service.query.BankQuery;

/**
 * 银行管理
 * 
 */
public abstract interface BankManage extends Service {
	/**
	 * 新增银行信息
	 * 
	 * @param bank
	 * @throws Throwable
	 */
	public abstract int add(String bankName, String code) throws Throwable;

	/**
	 * 查询银行列表信息
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T5020> search(BankQuery query, Paging paging)
			throws Throwable;

	/**
	 * 修改银行信息
	 * 
	 * @param bankId
	 * @param bank
	 * @throws Throwable
	 */
	public abstract void update(int id, String bankName, String code) throws Throwable;

	/**
	 * 根据ID得到银行信息
	 * 
	 * @param bankId
	 * @return
	 * @throws Throwable
	 */
	public abstract T5020 get(int bankId) throws Throwable;

	/**
	 * 根据ID、修改银行状态启用
	 * 
	 * @param bankId
	 * @param bankStatus
	 * @throws Throwable
	 */
	public abstract void enable(int bankId) throws Throwable;

	/**
	 * 根据ID、修改银行状态停用
	 * 
	 * @param bankId
	 * @param bankStatus
	 * @throws Throwable
	 */
	public abstract void disable(int bankId) throws Throwable;

	/**
	 * 判断银行卡是否存在
	 * 
	 * @param bankId
	 * @param bankStatus
	 * @throws Throwable
	 */
	public abstract boolean isBankExists(String name) throws Throwable;

}
