package com.dimeng.p2p.modules.bid.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.modules.bid.console.service.entity.CjRecord;
import com.dimeng.p2p.modules.bid.console.service.entity.Fksh;
import com.dimeng.p2p.modules.bid.console.service.query.CjRecordQuery;
import com.dimeng.p2p.modules.bid.console.service.query.FkshQuery;

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
     * 描述： 查询放款审核列表借款总金额,投资总金额,红包总金额,体验金总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract Fksh searchAmount(FkshQuery query)
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
	public void checkBatch(int[] ids, T6230_F20 status) throws Throwable;

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
	public void check(int id, T6230_F20 status, String des) throws Throwable;

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
     * 查询放款成交记录借款总金额,投资总金额,红包总金额,体验金总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract CjRecord searchCjAmount(CjRecordQuery query)
        throws Throwable;

	/**
	 * 统计借款总金额
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract BigDecimal totalAmount() throws Throwable;
	/**
	 * 统计放款总成交金额
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract BigDecimal totalFkAmount() throws Throwable;


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
    
    /**
     * 
     * <dl>
     * 描述：导出放款审核列表.
     * </dl>
     * @param paramArrayOfYFundRecord 审核记录数组
     * @param outputStream 输出流
     * @param charset 导出编码格式
     * @return
     * @throws Throwable
     */
    public abstract void exportFkshInfo(Fksh[] paramArrayOfYFundRecord, OutputStream outputStream, String charset)
        throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeLog(String type, String log)
        throws Throwable;

}
