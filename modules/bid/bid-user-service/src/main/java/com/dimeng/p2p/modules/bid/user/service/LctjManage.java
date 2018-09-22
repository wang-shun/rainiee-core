package com.dimeng.p2p.modules.bid.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S70.entities.T7203;
import com.dimeng.p2p.modules.bid.user.service.entity.*;

import java.io.OutputStream;

/**
 * 理财统计总数
 */
public abstract interface LctjManage extends Service{
	/**
	 * 理财统计总数
	 * @return EarnFinancingTotal
	 */
	public EarnFinancingTotal getEarnFinancingTotal(String sYear,String sMonth,String eYear,String eMonth) throws Throwable;


	/**
	 * 理财统计列表
	 * @return PagingResult
	 * @throws Throwable
	 */
	public abstract PagingResult<EarnFinancingInfo> search(String sYear,String sMonth,String eYear,String eMonth,Paging paging)throws Throwable;

	/**
	 * 理财统计-详情列表
	 * @return PagingResult
	 * @throws Throwable
	 */
	public abstract PagingResult<T7203> searchDetail(String sYear,String sMonth,String eYear,String eMonth,Paging paging)throws Throwable;
	
	/**
     * 理财统计
     * @return PagingResult
     * @throws Throwable
     */
    public abstract PagingResult<T7203> searchStatistics(String year,String month,String type,Paging paging)throws Throwable;
	
	/**
	 * 理财统计-累计投资金额详情列表
	 * @return PagingResult
	 * @throws Throwable
	 */
	public abstract PagingResult<InvestAmount> searchInvestAmountDetail(String year,String month,Paging paging)throws Throwable;

	/**
	 * 理财统计导出
	 * @param recWits
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public abstract void export(T7203[] recWits, OutputStream outputStream, String charset) throws Throwable;
}
