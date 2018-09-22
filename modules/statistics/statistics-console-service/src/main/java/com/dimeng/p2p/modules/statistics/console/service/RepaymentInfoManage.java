package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.RepaymentStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.RepaymentStatisticsQuery;

/**
 * 
 * 还款统计表接口（包括还款中、逾期待还、垫付待还、已还）
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月8日]
 */
public abstract interface RepaymentInfoManage extends Service
{
    /**
     * 还款中列表查询
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<RepaymentStatisticsEntity> getRepaymentList(RepaymentStatisticsQuery query,
        Paging paging)
        throws Throwable;
    
    /**
     * 还款中列表金额统计
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal getRepaymentTotal(RepaymentStatisticsQuery query)
        throws Throwable;
    
    /**
     * 导出还款列表金额统计
     * <功能详细描述>
     * @param recWits
     * @param total
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void export(RepaymentStatisticsEntity[] recWits, BigDecimal total, OutputStream outputStream,
        String charset)
        throws Throwable;

    /**
     * 逾期待还列表查询
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<RepaymentStatisticsEntity> getOverdueList(RepaymentStatisticsQuery query,
        Paging paging)
        throws Throwable;
    
    /**
     * 逾期待还列表金额统计
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract RepaymentStatisticsEntity getOverdueTotal(RepaymentStatisticsQuery query)
        throws Throwable;
    
    /**
     * 导出逾期待还列表
     * <功能详细描述>
     * @param recWits
     * @param total
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportOverdue(RepaymentStatisticsEntity[] recWits, RepaymentStatisticsEntity overdueTotal,
        OutputStream outputStream,
        String charset)
        throws Throwable;

    /**
     * 垫付待还列表查询
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<RepaymentStatisticsEntity> getPaymentAccountList(RepaymentStatisticsQuery query,
        Paging paging)
        throws Throwable;
    
    /**
     * 垫付待还列表金额统计
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract RepaymentStatisticsEntity getPaymentAccountTotal(RepaymentStatisticsQuery query)
        throws Throwable;
    
    /**
     * 导出垫付待还列表
     * <功能详细描述>
     * @param recWits
     * @param total
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportPaymentAccount(RepaymentStatisticsEntity[] recWits,
        RepaymentStatisticsEntity paymentAccountTotal,
        OutputStream outputStream, String charset)
        throws Throwable;

    /**
     * 已还列表查询
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<RepaymentStatisticsEntity> getAlreadyList(RepaymentStatisticsQuery query,
        Paging paging)
        throws Throwable;
    
    /**
     * 已还列表金额统计
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal getAlreadyTotal(RepaymentStatisticsQuery query)
        throws Throwable;
    
    /**
     * 导出已还列表金额
     * <功能详细描述>
     * @param recWits
     * @param total
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportAlready(RepaymentStatisticsEntity[] recWits, BigDecimal total,
        OutputStream outputStream,
        String charset)
        throws Throwable;
}
