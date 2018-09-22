package com.dimeng.p2p.modules.financing.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditFiles;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.UserInfo;
/**
 * 理财公用登陆用户信息接口
 *
 */
public interface UserInfoManage extends Service {

	/**
	 * 获取登陆用户的信息
	 * @return
	 * @throws Throwable
	 */
	public abstract UserInfo search()throws Throwable ;
	
	/**
	 * 根据身份证获取年龄和性别(1:男，0:女)
	 * @param card
	 * @return
	 * @throws Throwable
	 */
	public abstract UserInfo getAgeSex(String card)throws Throwable ;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询用户是否有逾期款未还.
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
	 * <li></li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6041}表，{@code F10 < SYSDATE()}
	 * </li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6041.F10}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link Y}对应{@code DATEDIFF(NOW(),F10)>0}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * @return {@link String} 用户的验证信息
	 * @throws Throwable
	 */
	public abstract String isYuqi() throws Throwable;
	
	/**
	 * 获取用户信用档案
	 * @param 借款标id
	 * @return
	 */
	public abstract CreditFiles getFile(int id)throws Throwable;
	/**
	 * 查询用户信息
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	public abstract UserInfo search(int userId)throws Throwable;
	/**
	 * 查询用户的借款列表
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<CreditInfo> searchCredit(int userId,Paging paging) throws Throwable;
}
