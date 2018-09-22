package com.dimeng.p2p.modules.finance.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.finance.console.service.entity.Yhzjgl;
import com.dimeng.p2p.modules.finance.console.service.entity.YhzjglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.YhzjglRecordQuery;

public interface YhzjManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：用户资金统计记录
	 * </dl>
	 * </dt>
	 * 
	 * @return UserFund 用户资金对象
	 * @throws Throwable
	 */
	public Yhzjgl getUserFundInfo() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询用户资金列表
	 * </dl>
	 * </dt>
	 * 
	 * @param query
	 *            用户资金查询接口
	 * @param page
	 *            分页对象
	 * @return PagingResult<UserFundRecord> 用户资金分页集合
	 * @throws Throwable
	 */
	public PagingResult<YhzjglRecord> serarch(YhzjglRecordQuery query,
			Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出用户资金查询记录
	 * </dl>
	 * </dt>
	 * 
	 * @param userFundRecord
	 *            用户资金分页集合
	 * @param outputStream
	 *            字节输出流
	 * @param charset
	 *            字符集
	 * 
	 * @return
	 * 
	 * @throws Throwable
	 */
	public void export(YhzjglRecord[] userFundRecord,
			OutputStream outputStream, String charset) throws Throwable;

}
