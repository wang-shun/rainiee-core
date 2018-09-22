package com.dimeng.p2p.modules.financing.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.financing.user.service.entity.AutoBidSet;
import com.dimeng.p2p.modules.financing.user.service.query.AutoBidQuery;

/**
 * 自动理财工具接口
 *
 */
public abstract interface AutoUtilFinacingManage extends Service{
	
	/**
	 * 保存投资设置
	 * @throws Throwable
	 */
	public abstract void save(AutoBidQuery  autoBidQuery) throws Throwable;
	/**
	 * 关闭自动设置
	 * @throws Throwable
	 */
	public abstract void close() throws Throwable;
	/**
	 * 查询自动投资信息
	 * @return
	 * @throws Throwable
	 */
	public abstract AutoBidSet search() throws Throwable;
	
	
}
