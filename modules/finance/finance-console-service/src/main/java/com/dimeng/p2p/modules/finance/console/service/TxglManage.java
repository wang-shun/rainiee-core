package com.dimeng.p2p.modules.finance.console.service;

import java.io.InputStream;
import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.WithdrawStatus;
import com.dimeng.p2p.modules.finance.console.service.entity.Bank;
import com.dimeng.p2p.modules.finance.console.service.entity.Txgl;
import com.dimeng.p2p.modules.finance.console.service.entity.TxglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.TxglRecordQuery;

public interface TxglManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述： 提现统计记录
	 * </dl>
	 * </dt>
	 * 
	 * @return Extraction 提现实体对象
	 * @throws Throwable
	 */
	public Txgl getExtractionInfo() throws Throwable;

	/**
	 * 查询所有银行卡
	 * 
	 * @return
	 * @throws Throwable
	 */
	public Bank[] getBanks() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询提现列表
	 * </dl>
	 * </dt>
	 * 
	 * @param query
	 *            提现查询接口
	 * @param paging
	 *            分页对象
	 * @return PagingResult<UserExtractionRecord> 提现分页集合
	 * @throws Throwable
	 */
	public PagingResult<TxglRecord> search(TxglRecordQuery query, Paging paging)
			throws Throwable;

	/**
	 * 查询提现详细
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public TxglRecord get(int id) throws Throwable;

	/**
	 * 导入提现
	 * 
	 * @param inputStream
	 * @param real_name
	 * @param charset
	 * @throws Throwable
	 */
	public void importData(InputStream inputStream, String real_name,
			String charset) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 批量审核提现记录
	 * </dl>
	 * </dt>
	 * 
	 * @param serialNumberItem
	 *            提现流水号集合
	 * @param flag
	 *            审核标志
	 * @throws Throwable
	 */
	public void checkBatch(WithdrawStatus status, String check_reason,
			int... ids) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 单个审核提现记录
	 * </dl>
	 * </dt>
	 * 
	 * @param serialNumber
	 *            提现流水号
	 * @param flag
	 *            审核标志
	 * @throws Throwable
	 */
	public void check(WithdrawStatus status, String check_reason, int id)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出提现审核通过
	 * </dl>
	 * </dt>
	 * 
	 * @param txglRecord
	 *            审核通过记录
	 * @throws Throwable
	 */
	public void export(TxglRecord[] txglRecord, OutputStream outputStream,
			String charset) throws Throwable;
}
