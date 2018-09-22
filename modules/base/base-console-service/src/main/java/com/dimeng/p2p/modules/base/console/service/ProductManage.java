package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.modules.base.console.service.query.ProductQuery;

/**
 * 标产品管理
 * 
 */
public abstract interface ProductManage extends Service {
	/**
	 * 新增产品信息
	 * 
	 * @param t6216
	 * @throws Throwable
	 */
	public abstract int add(T6216 t6216) throws Throwable;

	/**
	 * 查询产品列表信息
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6216> search(ProductQuery query, Paging paging)
			throws Throwable;

	/**
	 * 修改产品信息
	 * 
	 * @param bankId
	 * @param bank
	 * @throws Throwable
	 */
	public abstract void update(T6216 t6216) throws Throwable;

	/**
	 * 根据ID得到产品信息
	 * 
	 * @param productId
	 * @return
	 * @throws Throwable
	 */
	public abstract T6216 get(int productId) throws Throwable;

	/**
	 * 根据ID、修改产品状态启用
	 * 
	 * @param productId
	 * @throws Throwable
	 */
	public abstract void enable(int productId) throws Throwable;

	/**
	 * 根据ID、修改产品状态停用
	 * 
	 * @param productId
	 * @throws Throwable
	 */
	public abstract void disable(int productId) throws Throwable;

	/**
	 * 判断产品是否存在
	 * 
	 * @param name
	 * @throws Throwable
	 */
	public abstract boolean isProductExists(String name) throws Throwable;
	
	
	/**
	 * 判断产品是否存在
	 * 
	 * @param name
	 * @throws Throwable
	 */
	public abstract int getValidProdcutsCount() throws Throwable;

}
