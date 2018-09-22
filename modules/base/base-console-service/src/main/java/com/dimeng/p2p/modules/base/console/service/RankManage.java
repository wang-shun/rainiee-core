package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5127;

/**
 *  等级管理
 * 
 */
public abstract interface RankManage extends Service {
	
	/**
	 * 分页查询等级信息
	 * @param entity
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T5127> seach(T5127 entity,Paging paging) throws Throwable;
	
	/**
	 * 新增等级信息
	 * @param entity
	 * @throws Throwable
	 */
	public abstract void add(T5127 entity) throws Throwable;
	
	/**
	 * 修改等级信息
	 * @param entity
	 * @throws Throwable
	 */
	public abstract void update(T5127 entity) throws Throwable;
	
	/**
	 * 启用等级信息
	 * @param id
	 * @throws Throwable
	 */
	public abstract void updateQy(int id) throws Throwable;
	
	/**
	 * 停用等级信息
	 * @param id
	 * @throws Throwable
	 */
	public abstract void updateTy(int id) throws Throwable;
	/**
	 * 根据id得到等级信息
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T5127 get(int id) throws Throwable;
}
