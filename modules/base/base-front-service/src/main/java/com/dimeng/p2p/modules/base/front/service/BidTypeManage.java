package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;

/**
 * 标类型管理
 */
public abstract interface BidTypeManage extends Service {
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据标ID查询类型名称
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
	public abstract String getName(int id) throws Throwable;
}
