package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5123;
import com.dimeng.p2p.S51.enums.T5123_F04;
import com.dimeng.p2p.modules.base.console.service.entity.AttestationQuery;

/**
 * 信用认证项管理
 */
public abstract interface CreditCertificationManage extends Service {
	/**
	 * 分页查询认证项
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T5123> search(AttestationQuery query,
			Paging paging) throws Throwable;
	
	/**
	 * 判断信用认证名称是否存在，存在返回T，不存在返回F
	 * @param name
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isExist(T5123 entity) throws Throwable;


	/**
	 * 添加认证信息
	 * 
	 * @param query
	 * @return
	 * @throws Throwable
	 */
	public abstract int add(T5123 t5123) throws Throwable;

	/**
	 * 更新认证信息
	 * 
	 * @param query
	 * @return
	 * @throws Throwable
	 */
	public abstract void update(T5123 t5123) throws Throwable;

	/**
	 * 根据ID获取认证信息
	 * 
	 * @param query
	 * @throws Throwable
	 */
	public abstract T5123 get(int id) throws Throwable;

	/**
	 * 更新认证状态
	 * 
	 * @param id
	 * @param state
	 * @return
	 * @throws Throwable
	 */
	public abstract void update(int id, T5123_F04 state) throws Throwable;
}
