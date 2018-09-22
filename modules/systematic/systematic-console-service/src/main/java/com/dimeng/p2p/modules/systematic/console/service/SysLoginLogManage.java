package com.dimeng.p2p.modules.systematic.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.systematic.console.service.entity.OperLog;
import com.dimeng.p2p.modules.systematic.console.service.entity.SysLog;
import com.dimeng.p2p.modules.systematic.console.service.query.OperLogQuery;
import com.dimeng.p2p.modules.systematic.console.service.query.SysLogQuery;

public abstract interface SysLoginLogManage extends Service {
	/**
	 * 
	 * <dl>
	 * 描述：根据条件分页显示所有系统用户登录日志
	 * </dl>
	 * 
	 * 
	 * @param query
	 *            查询条件
	 * @param paging
	 *            分页参数
	 * @return {@link PagingResult}{@code <}{@link SysLog}{@code >} 查询结果
	 * @throws Throwable
	 */
	public abstract PagingResult<SysLog> seacrch(SysLogQuery query,
			Paging paging) throws Throwable;

	/**
	 * 查询后台操作日志
	 */
	public abstract PagingResult<OperLog> search(OperLogQuery query,
			Paging paging) throws Throwable;
}
