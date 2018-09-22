package com.dimeng.p2p.modules.account.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.entities.T7168;
import com.dimeng.p2p.modules.account.console.service.entity.Grxx;

/**
 * 客户经理管理
 * 
 */
public abstract interface KhjlManage extends Service {
	
	
	/**
	 * 给用户设置客户经理
	 * @param id 用户id
	 * @param name 客户经理登录
	 * @throws Throwable
	 */
	public abstract void add(int userId,String name) throws Throwable;  
	
	/**
	 * 通过用户id判断用户是否已存在客户经理
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isExist(int userId) throws Throwable;
	
	/**
	 * 分页查询当前登录的客户经理的用户
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Grxx> search(Grxx grxx,Paging paging) throws Throwable;
	
	/**
	 * 根据用户id分页查询回访记录
	 * @param userID
	 * @param pagin
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T7168> searchHfjl(int userID ,Paging paging) throws Throwable;
	
	/**
	 * 新增回访记录
	 * @param entity
	 * @throws Throwable
	 */
	public abstract void addHfjl(T7168 entity) throws Throwable;
	 
}
