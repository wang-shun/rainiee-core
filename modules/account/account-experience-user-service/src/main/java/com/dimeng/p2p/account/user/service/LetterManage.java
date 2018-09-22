package com.dimeng.p2p.account.user.service;

import java.sql.SQLException;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.account.user.service.entity.LetterEntity;
import com.dimeng.p2p.common.enums.LetterStatus;

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
	public abstract PagingResult<LetterEntity> search(LetterStatus letterRead, Paging paging)
			throws Throwable;
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
	public abstract void delete(int... letterID) throws Throwable;
	/**
	 * 获取当前登录用户站内信条数.
	 * @return
	 * @throws Throwable
	 */
	public abstract int getCount() throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 将站内信更新为已读状态
	 * </dl>
	 * <dl>
	 * <ol>
	 * <li>参数id小于等于0时，直接返回</li>
	 * <li>更新表T6035的F06字段为'YD'</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            站内信id，对应表T6035的F01字段
	 * @throws SQLException
	 */
	public abstract void updateToRead(int id) throws Throwable;

	/**
	 * 获取站内信，根据站内信ID、用户ID
	 * @param letterID
	 * @return
	 * @throws Throwable
	 */
	public abstract int getT6123(int letterID) throws Throwable;
	
	/**
	 * 根据ID更改站内信为已读
	 * @param letterID 站内信ID
	 * @throws Throwable
	 */
	public abstract void updateYd(int... letterID) throws Throwable;
}
