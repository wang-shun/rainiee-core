package com.dimeng.p2p.modules.salesman.console.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.salesman.console.service.entity.Customers;
import com.dimeng.p2p.modules.salesman.console.service.entity.Salesman;
import com.dimeng.p2p.modules.salesman.console.service.query.CustomersQuery;
import com.dimeng.p2p.modules.salesman.console.service.query.SalesmanQuery;
/**
 * 业务员管理
 * @author gaoshaolong
 *
 */
public interface SalesmanManage extends Service{
	/**
	 * 获取业务员列表
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Salesman> search(SalesmanQuery query,
			Paging paging) throws Throwable;
	
	/**
	 * 导出业务员信息
	 * @param query
	 * @throws Throwable
	 */
	public abstract Salesman[] search(SalesmanQuery query) throws Throwable;

	/**
	 * 添加业务员
	 * @param query
	 * @throws Throwable
	 */
	public abstract void add(SalesmanQuery query) throws Throwable;
	/**
	 * 修改业务员
	 * @param query
	 * @throws Throwable
	 */
	public abstract void update(SalesmanQuery query) throws Throwable;
	
	/**
	 * 查询业务员
	 * @param query
	 * @throws Throwable
	 */
	public abstract Salesman search(String ywyId) throws Throwable;
	
	/**
	 * 查询手机号是否存在
	 * @param query
	 * @throws Throwable
	 */
	public abstract String searchTel(String tel) throws Throwable;
	
	/**
	 * 根据条件查询业务信息
	 * @param query
	 * @throws Throwable
	 */
	public abstract PagingResult<Customers> search(CustomersQuery query, Paging paging) throws Throwable;
	
	/**
	 * 导出业务信息
	 * @param query
	 * @throws Throwable
	 */
	public abstract Customers[] search(CustomersQuery query) throws Throwable;
	/**
	 * 查询累计总额
	 * @param query
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal ljTotle(CustomersQuery query)throws Throwable;

}
