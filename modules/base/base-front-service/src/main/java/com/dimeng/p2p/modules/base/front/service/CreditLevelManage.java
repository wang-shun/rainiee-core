package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S51.entities.T5124;

/**
 * 信用等级管理
 */
public abstract interface CreditLevelManage extends Service {

	/**
	 *查询信用等级
	 * @return
	 */
	public abstract T5124[] searchAll()throws Throwable;
	
	/**
	 * 根据信用积分得到信用等级信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract String getXydj(int scroe) throws Throwable;
	
	/**
	 * 根据信用积分得到信用等级ID
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract int getId(int scroe) throws Throwable;
	
	/**
	 * 根据信用id得到信用详情
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract String get(int id) throws Throwable;
	
}
