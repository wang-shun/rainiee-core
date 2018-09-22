package com.dimeng.p2p.modules.financial.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.financial.console.service.entity.Creditor;
import com.dimeng.p2p.modules.financial.console.service.query.CreditorQuery;
import com.dimeng.p2p.modules.financial.console.service.query.RichCreditorQuery;

public interface CreditorManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询投资中的债权列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param creditorQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 债权列表信息
	 */
	public abstract PagingResult<Creditor> creditorTbzSearch(CreditorQuery creditorQuery, Paging paging)
			throws Throwable;
    
    /**
     * 更具条件查询投资中的债权总金额
     * <功能详细描述>
     * @param creditorQuery
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal creditorTbzAmountCount(CreditorQuery creditorQuery)
        throws Throwable;
	
	public abstract PagingResult<Creditor> creditorTbzRichSearch(RichCreditorQuery creditorQuery, Paging paging)
			throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询回收中的债权列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param creditorQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 债权列表信息
	 */
	public abstract PagingResult<Creditor> creditorYfkSearch(CreditorQuery creditorQuery, Paging paging)
			throws Throwable;
    
    /**
     * 更具条件查询回款中的债权总金额
     * <功能详细描述>
     * @param creditorQuery
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal creditorYfkAmountCount(CreditorQuery creditorQuery)
        throws Throwable;

	public abstract PagingResult<Creditor> creditorYfkRichSearch(RichCreditorQuery creditorQuery, Paging paging)
			throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询已结清的债权列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param creditorQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 债权列表信息
	 */
	public abstract PagingResult<Creditor> creditorYjqSearch(CreditorQuery creditorQuery, Paging paging)
			throws Throwable;
    
    /**
     * 更具条件查询已结清的债权总金额
     * <功能详细描述>
     * @param creditorQuery
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal creditorYjqAmountCount(CreditorQuery creditorQuery)
        throws Throwable;

	public abstract PagingResult<Creditor> creditorYjqRichSearch(RichCreditorQuery creditorQuery, Paging paging)
			throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询已转出的债权列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param creditorQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 债权列表信息
	 */
	public abstract PagingResult<Creditor> creditorYzcSearch(CreditorQuery creditorQuery, Paging paging)
			throws Throwable;
    
    /**
     * 更具条件查询已转出的债权总金额
     * <功能详细描述>
     * @param creditorQuery
     * @return
     * @throws Throwable
     */
    public abstract Creditor creditorYzcAmountCount(CreditorQuery creditorQuery)
        throws Throwable;

	public abstract PagingResult<Creditor> creditorYzcRichSearch(RichCreditorQuery creditorQuery, Paging paging)
			throws Throwable;
	
	/**
	 * 导出回收中的债权
	 * @param users
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public abstract void export(Creditor[] creditors, OutputStream outputStream,
			String charset) throws Throwable;
	
	/**
	 * 导出投资中的债权
	 * @param creditors
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public abstract void exportTbz(Creditor[] creditors, OutputStream outputStream,
			String charset) throws Throwable;
	
	/**
	 * 导出已结清的债权
	 * @param creditors
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public abstract void exportYjq(Creditor[] creditors, OutputStream outputStream,
			String charset) throws Throwable;
	
	/**
	 * 导出已转出的债权
	 * @param creditors
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public abstract void exportYzc(Creditor[] creditors, OutputStream outputStream,
			String charset) throws Throwable;
	
}
