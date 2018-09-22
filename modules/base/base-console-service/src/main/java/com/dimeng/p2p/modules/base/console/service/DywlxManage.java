package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5131;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S62.entities.T6213;
import com.dimeng.p2p.S62.entities.T6214;
import com.dimeng.p2p.S62.enums.T6213_F03;

/**
 * 抵押物类型管理
 */
public abstract interface DywlxManage extends Service {
	/**
	 * 新增抵押物类型信息
	 * 
	 * @param t6213
	 * @throws Throwable
	 */
	public abstract void add(T6213 t6213) throws Throwable;

	/**
	 * 查询抵押物类型列表信息
	 * 
	 * @param f02
	 * @param f03
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6213> search(String f02, T6213_F03 f03,
			Paging paging) throws Throwable;

	/**
	 * 修改抵押物类型信息
	 * 
	 * @param t6213
	 * @throws Throwable
	 */
	public abstract void update(T6213 t6213) throws Throwable;

	/**
	 * 根据ID得到抵押物类型信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6213 get(int id) throws Throwable;

	/**
	 * 修改抵押物类型状态
	 * 
	 * @param id
	 * @param f03
	 * @throws Throwable
	 */
	public abstract void update(int id, T6213_F03 f03) throws Throwable;

	/**
	 * 查询所有抵押物类型
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract T6213[] getDyws() throws Throwable;

	/**
	 * 根据抵押物类型ID查询抵押物属性
	 * 
	 * @return
	 * @throws Throwable
	 */
	public T6214[] getDywsx(int typeId) throws Throwable;

	/**
	 * 根据属性ID查询属性名称
	 */
	public String getNameById(int id) throws Throwable;
	
	/**
	 * 根据抵押物类型ID查找抵押物属性是否停用。 启用返回T，停用返回F
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public boolean isAllTy(int id) throws Throwable;

	/**
	 * 得到平台垫付类型
	 *
	 * @return
	 * @throws Throwable
	 */
	public T5131 getPtdfType() throws Throwable;

	/**
	 * 更改平台垫付类型
	 *
	 * @return
	 * @throws Throwable
	 */
	public void updateT5031(int id,T5131_F02 f02) throws Throwable ;
}
