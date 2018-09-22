package com.dimeng.p2p.modules.base.front.service;

import java.sql.SQLException;

import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5019;

/**
 * 区划管理
 */
public abstract interface DistrictManage extends Service {

	/**
	 * 根据ID查询省信息
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract T5019 getShengName(int id) throws Throwable;

	/**
	 * 查询所有省
	 * 
	 * @return
	 */
	public abstract T5019[] getSheng() throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据省ID查询所有市.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>...</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param shengId
	 * @return
	 * @throws Throwable
	 */
	public abstract T5019[] getShi(int shengId) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据市ID查询县.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>...</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param shiId
	 * @return
	 * @throws Throwable
	 */
	public abstract T5019[] getXian(int shiId) throws Throwable;

	/**
	 * 通过id的信息
	 * @param areaId
	 * @return
	 * @throws Throwable
	 */
	public abstract T5019 getArea(int areaId) throws Throwable;

	/**
	 * 支持中文名字模糊查询，支持首字母查询
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 * @throws ResourceNotFoundException
	 */
	public abstract PagingResult<T5019> searchXIAN(String name, Paging paging)
			throws ResourceNotFoundException, SQLException;
}
