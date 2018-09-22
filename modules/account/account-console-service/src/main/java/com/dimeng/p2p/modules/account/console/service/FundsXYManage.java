package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.modules.account.console.service.entity.FundsXYJYView;
import com.dimeng.p2p.modules.account.console.service.entity.FundsXYView;
import com.dimeng.p2p.modules.account.console.service.query.FundsJYQuery;
import com.dimeng.p2p.modules.account.console.service.query.FundsXYQuery;

public interface FundsXYManage extends Service{
	/**
	 * 信用账户信息查询
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<FundsXYView> search(FundsXYQuery query,
			Paging paging) throws Throwable;
    
    /**
     * 信用账户信息查询授信总额度
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal searchAmount(FundsXYQuery query)
        throws Throwable;
	/**
	 * 信用流水搜索查询
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<FundsXYJYView> xyjlSearch(FundsJYQuery query,
			Paging paging) throws Throwable;
	/**
	 * 信用账户导出
	 * @param FundXYRecord
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	 public abstract void export(FundsXYView[] FundXYRecord, OutputStream outputStream, String charset)
			    throws Throwable;
	 /**
	  * 信用流水导出
	  * @param FundXYJYRecord
	  * @param outputStream
	  * @param charset
	  * @throws Throwable
	  */
	 public abstract void export(FundsXYJYView[] FundXYJYRecord, OutputStream outputStream, String charset)
			    throws Throwable;
	 public abstract T5122[] getTradeTypes()throws Throwable;
}
