package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.enums.T6211_F03;

/**
 * 标类型管理
 */
public abstract interface BlxManage extends Service {
	/**
	 * 新增标类型信息
	 * 
	 * @param t6211
	 * @throws Throwable
	 */
	public abstract void add(T6211 t6211) throws Throwable;

	/**
	 * 查询标类型列表信息
	 * 
	 * @param f02
	 * @param f03
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6211> search(String f02, T6211_F03 f03,
			Paging paging) throws Throwable;

	/**
	 * 修改标类型信息
	 * 
	 * @param t6211
	 * @throws Throwable
	 */
	public abstract void update(T6211 t6211) throws Throwable;

	/**
	 * 根据ID得到标类型信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6211 get(int id) throws Throwable;

	/**
	 * 修改标类型状态
	 * 
	 * @param id
	 * @param f03
	 * @throws Throwable
	 */
	public abstract void update(int id, T6211_F03 f03) throws Throwable;
	
	/**
	 * 判断表类型是否存在，存在返回T，不存在返回F
	 * @param name
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isExist(T6211 entity) throws Throwable;
}
