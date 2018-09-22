package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S51.enums.T5122_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.modules.base.console.service.entity.TradetypeQuery;

/**
 * 交易类型管理
 * 
 */
public abstract interface TradeTypeManage extends Service {
	/**
	 * 分页查询
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T5122> search(TradetypeQuery query,
			Paging paging) throws Throwable;

	/**
	 * 添加
	 * 
	 * @param t5122
	 * @return
	 * @throws Throwable
	 */
	public abstract int add(T5122 t5122) throws Throwable;

	/**
	 * 更新
	 * 
	 * @param t5122
	 * @return
	 * @throws Throwable
	 */
	public abstract void update(T5122 t5122) throws Throwable;

	/**
	 * 查询
	 * 
	 * @param id
	 * @throws Throwable
	 */
	public abstract T5122 get(int id) throws Throwable;

	/**
	 * 启用/停用
	 * 
	 * @param status
	 * @throws Throwable
	 */
	public abstract void update(int id, T5122_F03 status) throws Throwable;

	/**
	 * 根据查询条件，查询属于个人、企业、机构、平台等的交易类型
	 * @param userType
	 * @param t6110_F10
	 * @return
	 * @throws Throwable
	 */
	public abstract T5122[] search(T6110_F06 userType, T6110_F10 t6110_F10, Object ojbect) throws Throwable;
}
