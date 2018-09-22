package com.dimeng.p2p.account.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6123;
import com.dimeng.p2p.S61.enums.T6123_F05;

public abstract interface LetterManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：获取当前登录用户未读站内信条数.
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
	 * @return
	 * @throws Throwable
	 */
	public abstract int getUnReadCount() throws Throwable;
	/**
	 * 根据查询条件分页查询站内信
	 * @param letterType 站内信状态
	 * @param paging 分页参数
	 * @return  
	 * @throws Throwable
	 */
	public abstract PagingResult<T6123> seach(T6123_F05 letterType,Paging paging) throws Throwable;
	/**
	 * 根据ID得到站内信内容
	 * @param letterID 站内信ID
	 * @return String 站内信内容
	 * @throws Throwable
	 */
	public abstract String get(int letterID) throws Throwable;
	/**
	 * 根据ID更改站内信状态(删除)
	 * @param letterID 站内信ID
	 * @throws Throwable
	 */
	public abstract void del(int ... letterID) throws Throwable;
	
}
