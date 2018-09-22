package com.dimeng.p2p.modules.bid.user.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.user.service.entity.CreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.MonthCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.NewCreditList;
import com.dimeng.p2p.modules.bid.user.service.entity.NewCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.NotPayCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.PayCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.YearCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.query.NewCreditListQuery;

/**
 * 借款统计查询
 *
 */
public abstract interface JktjManage extends Service
{
    /**
     * <dt>
     * <dl>
     * 描述：查询借款统计信息 
     * </dl>
     * </dt>
     * @return
     */
    public CreditTotal getCreditTotal()
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：查询已还借款统计信息 
     * </dl>
     * </dt>
     * @return
     */
    public PayCreditTotal getPayCreditTotal()
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：最近一年您在微贷网成功借款统计
     * </dl>
     * </dt>
     * @return
     */
    public YearCreditTotal[] getYearCreditTotal()
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：待还统计
     * </dl>
     * </dt>
     * @return
     */
    public NotPayCreditTotal getNotPayCreditTotal()
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：最近六个月您在微贷网还款
     * </dl>
     * </dt>
     * @return
     */
    public MonthCreditTotal[] getMonthCreditTotal()
        throws Throwable;
    
    /**
     * 近6个月借款
     * @return
     * @throws Throwable
     */
    public BigDecimal getLgyjk()
        throws Throwable;
    
    /**
     * 近一年借款
     * @return
     * @throws Throwable
     */
    public BigDecimal getYnjk()
        throws Throwable;
    
    /**
     * 查询借款统计列表<一句话功能简述>
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<NewCreditList> getNewCreditList(NewCreditListQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询借款统计汇总数据<一句话功能简述>
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract NewCreditTotal getNewCreditTotal(NewCreditListQuery query)
        throws Throwable;

    /**
     * 借款统计导出
     * @param recWits
     * @param total
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void export(NewCreditList[] recWits, OutputStream outputStream, String charset) throws Throwable;
}
