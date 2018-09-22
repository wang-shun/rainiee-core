package com.dimeng.p2p.modules.systematic.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.entities.T6051;
import com.dimeng.p2p.modules.systematic.console.service.entity.UserLog;
import com.dimeng.p2p.modules.systematic.console.service.query.UserLogQuery;

public abstract interface UserLoginLogManage extends Service {
	/**
	 * 
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据条件分页显示所有用户登录日志
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
	 * <li>查询{@link T6051}表</li>
	 * <li>查询条件:
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link UserLogQuery#getAccountName()}不为空  则{@code T6051.F02 LIKE }{@link UserLogQuery#getAccountName()}</li>
	 * <li>如果{@link UserLogQuery#getCreateTimeStart()}不为空  则{@code DATE(T6051.F03)>=}{@link UserLogQuery#getCreateTimeStart()}</li>
	 * <li>如果{@link UserLogQuery#getCreateTimeEnd()}不为空  则{@code DATE(T6051.F03)<= }{@link UserLogQuery#getCreateTimeEnd()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T6051.F03}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link UserLog#id}对应{@code T6051.F01}</li>
	 * <li>{@link UserLog#accountName}对应{@code T6051.F02}</li>
	 * <li>{@link UserLog#lastLoginTime}对应{@code T6051.F03}</li>
	 * <li>{@link UserLog#lastIp}对应{@code T6051.F04}</li>
	 * <li>{@link UserLog#isSuccess}对应{@code T6051.F05}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>			
	 * 
	 * 
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link UserLog}{@code >} 分页查询结果
	 * @throws Throwable 
	 */
	public abstract PagingResult<UserLog> seacrch(UserLogQuery query, Paging paging) throws Throwable;
}
