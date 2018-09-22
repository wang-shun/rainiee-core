package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S51.enums.T5124_F05;
import com.dimeng.p2p.modules.base.console.service.query.CreditLevelQuery;

/**
 * 信用等级管理
 */
public abstract interface CreditLevelManage extends Service {
	/**
	 * 新增信用等级信息
	 * 
	 * @param creditLeve
	 * @throws Throwable
	 */
	public abstract void add(T5124 t5124) throws Throwable;
	
	
	/**
	 * 判断等级名称是否存在，存在返回T，不存在返回F
	 * @param name
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isExist(T5124 entity) throws Throwable;

	/**
	 * 查询信用等级列表信息
	 * 
	 * @param creditLeve
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T5124> search(CreditLevelQuery creditLeve,
			Paging paging) throws Throwable;

	/**
	 * 修改信用等级信息
	 * 
	 * @param creditLeve
	 * @throws Throwable
	 */
	public abstract void update(T5124 t5124) throws Throwable;

	/**
	 * 根据ID得到信用等级信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T5124 get(int id) throws Throwable;

	/**
	 * 修改信用等级状态
	 * 
	 * @param id
	 * @param state
	 * @throws Throwable
	 */
	public abstract void update(int id, T5124_F05 state) throws Throwable;
}
