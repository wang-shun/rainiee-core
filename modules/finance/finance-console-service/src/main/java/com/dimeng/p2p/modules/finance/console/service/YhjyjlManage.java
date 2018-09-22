package com.dimeng.p2p.modules.finance.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.finance.console.service.entity.Yhjyjl;
import com.dimeng.p2p.modules.finance.console.service.entity.YhjyjlRecord;
import com.dimeng.p2p.modules.finance.console.service.query.YhjyjlRecordQuery;

public interface YhjyjlManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：获取用户交易记录
	 * </dl>
	 * </dt>
	 * 
	 * @return UserTrade 用户交易对象
	 * @throws Throwable
	 */
	public Yhjyjl getUserTradeInfo(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询用户交易列表
	 * </dl>
	 * </dt>
	 * 
	 * @param query
	 *            用户交易查询接口
	 * @param page
	 *            分页对象
	 * @return PagingResult<UserTradeRecord> 用户交易分页集合
	 * @throws Throwable
	 */
	public PagingResult<YhjyjlRecord> search(YhjyjlRecordQuery query,
			Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出用户交易查询记录
	 * </dl>
	 * </dt>
	 * 
	 * @param userTradeRecord
	 *            用户交易分页集合
	 * @return
	 * 
	 * @throws Throwable
	 */
	public void export(YhjyjlRecord[] records, OutputStream outputStream,
			String charset) throws Throwable;

}
