package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.modules.account.console.service.entity.AmountTotle;
import com.dimeng.p2p.modules.account.console.service.entity.FundsView;
import com.dimeng.p2p.modules.account.console.service.entity.ZjDetailView;
import com.dimeng.p2p.modules.account.console.service.query.FundsQuery;
import com.dimeng.p2p.modules.account.console.service.query.GrzjDetailQuery;

public abstract interface FundsManage extends Service {
	
	/**
	 * 资金统计
	 * @return
	 */
	public abstract AmountTotle getTotle()throws Throwable;
	/**
	 * 
	 * <dl>
	 * 描述：查询资金列表.
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<FundsView> search(FundsQuery query,
			Paging paging) throws Throwable;
	
	/**
	 * 
	 * <dl>
	 * 描述：查询资金列表.
	 * 2014/8/4更新账号类型
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<FundsView> search(FundsQuery query,T6101_F03 zhlx,
			Paging paging) throws Throwable;

	/**
	 * 查询备用资金列表.
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<FundsView> bYSearch(FundsQuery query,
			Paging paging) throws Throwable;

	public abstract void export(FundsView[] paramArrayOfYFundRecord,
			OutputStream outputStream, String charset) throws Throwable;
	
	public abstract void exportZj(FundsView[] paramArrayOfYFundRecord,
			OutputStream outputStream, String charset) throws Throwable;
	
	/**
	 * 
	 * <dl>
	 * 描述：查询个人资金明细.
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<ZjDetailView> queryGrzjDetail(GrzjDetailQuery query,
			Paging paging) throws Throwable;
	
	/**
	 * 
	 * <dl>
	 * 描述：个人资金明细收入支出统计.
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract ZjDetailView queryAmountCount(GrzjDetailQuery query) throws Throwable;
	
    /**
      * 
      * <dl>
      * 描述：导出个人资金明细.
      * </dl>
      * @param ZjDetailView 资金明细数组
      * @param outputStream 输出流
      * @param charset 导出编码格式
      * @return
      * @throws Throwable
      */
	public abstract void exportGrzjDetail(ZjDetailView[] paramArrayOfYFundRecord,
			OutputStream outputStream, String charset) throws Throwable;
	
	/**
     * 
     * <dl>
     * 描述：导出平台资金明细.
     * </dl>
     * @param ZjDetailView 资金明细数组
     * @param outputStream 输出流
     * @param charset 导出编码格式
     * @return
     * @throws Throwable
     */
   public abstract void exportPtzjDetail(ZjDetailView[] paramArrayOfYFundRecord,
           OutputStream outputStream, String charset) throws Throwable;

}
