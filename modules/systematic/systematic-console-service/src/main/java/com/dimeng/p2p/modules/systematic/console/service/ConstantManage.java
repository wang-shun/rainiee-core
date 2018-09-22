/**
 * 
 */
package com.dimeng.p2p.modules.systematic.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.systematic.console.service.entity.Constant;
import com.dimeng.p2p.modules.systematic.console.service.query.ConstantLogQuery;

/**
 * @author guopeng
 * 
 */
public abstract interface ConstantManage extends Service {

	/**
	 * 添加系统常量修改日志 <dt>
	 * <dl>
	 * 描述：.
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
	 * @param key
	 * @param name
	 *            常量名称
	 * @param value1
	 *            修改前值
	 * @param value2
	 *            修改后值
	 * @throws Throwable
	 */
	public abstract void addConstantLog(String key, String name, String value1,
			String value2) throws Throwable;

	/**
	 * 查询系统常量修改日志 <dt>
	 * <dl>
	 * 描述：.
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
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Constant> search(ConstantLogQuery query,
			Paging paging) throws Throwable;
	
	/**
	 * 查看日志明细
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract Constant selectById(int id) throws Throwable;
	
}
