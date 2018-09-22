package com.dimeng.p2p.modules.finance.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.modules.finance.console.service.entity.CjRecord;
import com.dimeng.p2p.modules.finance.console.service.entity.Fksh;
import com.dimeng.p2p.modules.finance.console.service.query.CjRecordQuery;
import com.dimeng.p2p.modules.finance.console.service.query.FkshQuery;

public interface FkshManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询放款审核列表
	 * </dl>
	 * </dt>
	 * 
	 * @param query
	 *            放款查询接口
	 * @param page
	 *            分页对象
	 * @return PagingResult<LoanCheck> 放款分页集合
	 * @throws Throwable
	 */
	public PagingResult<Fksh> search(FkshQuery query, Paging paging)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 批量放款
	 * </dl>
	 * </dt>
	 * 
	 * @param creditorIdItem
	 *            债权id项
	 * @param flag
	 *            放款标志
	 * @throws Throwable
	 */
	public void checkBatch(int[] ids, CreditStatus status) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 单个放款
	 * </dl>
	 * </dt>
	 * 
	 * @param creditorId
	 *            序号
	 * @param flag
	 *            放款标志
	 * @throws Throwable
	 */
	public void check(int id, CreditStatus status, String des) throws Throwable;

	/**
	 * 查询放款成交记录
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<CjRecord> search(CjRecordQuery query,
			Paging paging) throws Throwable;

	/**
	 * 统计放款总成交金额
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract BigDecimal totalAmount() throws Throwable;

	/**
	 * 导出成交记录
	 * 
	 * @param records
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public void export(CjRecord[] records, OutputStream outputStream,
			String charset) throws Throwable;

}
