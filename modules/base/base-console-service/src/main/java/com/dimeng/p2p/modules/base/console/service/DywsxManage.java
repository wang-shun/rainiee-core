package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6214;

/**
 * 抵押物属性管理
 */
public abstract interface DywsxManage extends Service {
	/**
	 * 新增抵押物属性信息
	 * 
	 * @param t6214
	 * @throws Throwable
	 */
	public abstract void add(T6214 t6214) throws Throwable;
	
	/**
	 * 启用抵押物属性信息
	 * @param id
	 * @throws Throwable
	 */
	public abstract void qyDywsx(int id) throws Throwable;
	
	/**
	 * 停用抵押物属性信息
	 */
	public abstract void tyDywsx(int id) throws Throwable;
	
	
	/**
	 * 查询抵押物属性列表信息
	 * 
	 * @param f02
	 * @param f03
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6214> search(int f02, String f03,
			Paging paging) throws Throwable;

	/**
	 * 修改抵押物属性信息
	 * 
	 * @param t6214
	 * @throws Throwable
	 */
	public abstract void update(T6214 t6214) throws Throwable;

	/**
	 * 根据ID得到抵押物属性信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6214 get(int id) throws Throwable;
}
