package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.enums.T6212_F04;

/**
 * 标附件类型管理
 */
public abstract interface BfjlxManage extends Service {
	/**
	 * 新增标附件类型信息
	 * 
	 * @param t6212
	 * @throws Throwable
	 */
	public abstract void add(T6212 t6212) throws Throwable;

	/**
	 * 查询标附件类型列表信息
	 * 
	 * @param f02
	 * @param f04
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6212> search(String f02, T6212_F04 f04,
			Paging paging) throws Throwable;

	/**
	 * 修改标附件类型信息
	 * 
	 * @param t6212
	 * @throws Throwable
	 */
	public abstract void update(T6212 t6212) throws Throwable;

	/**
	 * 根据ID得到标附件类型信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6212 get(int id) throws Throwable;

	/**
	 * 修改标附件类型状态
	 * 
	 * @param id
	 * @param f04
	 * @throws Throwable
	 */
	public abstract void update(int id, T6212_F04 f04) throws Throwable;
}
