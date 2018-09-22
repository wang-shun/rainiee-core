package com.dimeng.p2p.modules.bid.user.service;

import java.sql.SQLException;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.user.service.entity.HkEntity;
import com.dimeng.p2p.modules.bid.user.service.entity.LoanCount;
import com.dimeng.p2p.modules.bid.user.service.entity.RepayLoanDetail;

/**
 * 我的借款
 * 
 */
public abstract interface WdjkManage extends Service
{
    /**
     * <dt>
     * <dl>
     * 描述：查询借款统计信息
     * </dl>
     * </dt>
     * 
     * @return MyLoanCount 返回借款统计对象
     */
    public LoanCount getMyLoanCount()
        throws Throwable;
    
    /**
     * 还款中的借款
     * 
     * @return
     * @throws Throwable
     */
    public PagingResult<HkEntity> getHkzJk(Paging paging)
        throws Throwable;
    
    /**
     * app还款中
     * @return
     * @throws Throwable
     */
    public PagingResult<HkEntity> getMobileHkzJk(Paging paging)
        throws Throwable;
    
    /**
     * 已还清的借款
     * 
     * @return
     * @throws Throwable
     */
    public PagingResult<HkEntity> getYhqJk(Paging paging)
        throws Throwable;
    
    /**
     * app已还清的借款
     * @return
     * @throws Throwable
     */
    public PagingResult<HkEntity> getMobileYhqJk(Paging paging)
        throws Throwable;
    
    /**
     * 查询还款详细
     * 
     * @param loanId
     * @return
     * @throws Throwable
     */
    public PagingResult<RepayLoanDetail> getRepayLoanDetail(int loanId, Paging paging)
        throws Throwable;
    
    /**
     * 查询还款当期以前有没有未还贷款
     * 
     * @param loanId
     * @param repayId
     * @return
     * @throws SQLException
     */
    public boolean isWf(int loanId, int repayId)
        throws Throwable;
    
    /**
     * 判断是否逾期
     * 
     * @return boolean
     * @throws 
     */
    public boolean isYuqi()
        throws Throwable;
    
}
