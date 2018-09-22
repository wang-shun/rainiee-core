package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.modules.account.console.service.entity.FundsJYView;
import com.dimeng.p2p.modules.account.console.service.entity.UserTotle;
import com.dimeng.p2p.modules.account.console.service.query.FundsJYQuery;

public abstract interface FundsJYManage extends Service {
	/**
	 * 资金交易流水搜索查询
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<FundsJYView> search(FundsJYQuery query,
			Paging paging) throws Throwable;

	/**
	 * 资金交易流水导出
	 * 
	 * @param paramArrayOfYFundRecord
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public abstract void export(FundsJYView[] paramArrayOfYFundRecord,
			OutputStream outputStream, String charset) throws Throwable;

	public abstract List<T5122> getTradeTypes() throws Throwable;

	public T6101_F03 getType(int id) throws Throwable;
	
	public T6101_F03 getTypeT6101_F03(int id) throws Throwable;
	
	/**
	 * 获取用户的统计信息
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public UserTotle getUserTotle(int id) throws Throwable;
}
