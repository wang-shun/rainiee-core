/*
 * 文 件 名:  SubscribeBadClaimManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月15日
 */
package com.dimeng.p2p.repeater.claim;

import java.math.BigDecimal;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.repeater.claim.entity.SubscribeBadClaim;
import com.dimeng.p2p.repeater.claim.entity.SubscribeBadClaimTotal;
import com.dimeng.p2p.repeater.claim.entity.YZRLoanEntity;

/**
 * 
 * 认购不良债权管理接口类
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月15日]
 */
public interface SubscribeBadClaimManage extends Service
{
    /** 
     * 获取可认购的不良债权列表
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<SubscribeBadClaim> getCanBuyBadClaimList(Paging paging)
        throws Throwable;
    
    /** 
     * 获取已认购的不良债权列表
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<SubscribeBadClaim> getAlreadyBuyBadClaimList(Paging paging)
        throws Throwable;
    
    /** 
     * 获取认购的不良债权统计
     * @return
     * @throws Throwable
     */
    public SubscribeBadClaimTotal getSubscribeBadClaimTotal()
        throws Throwable;
    
    /** 
     * 购买不良债权
     * @param transferId
     * @return
     * @throws Throwable
     */
    public List<Integer> addOrder(int transferId)
        throws Throwable;
    
    /** 
     * 根据不良债权申请id获取债权价值
     * @param blzqId
     * @return
     * @throws Throwable
     */
    public BigDecimal getCreditPrice(int blzqId)
        throws Throwable;
    
    /** 
     * 根据不良债权申请id查询逾期天数
     * @param blzqId
     * @return
     * @throws Throwable
     */
    public int getOverdueDays(int blzqId)
        throws Throwable;
    
    /** 
     * 分页查询已转让的借款
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<YZRLoanEntity> getAlreadyTransferLoan(Paging paging)
        throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;
    
    /**
     * 判断用户是否网签
     * @param userId
     * @return
     * @throws Throwable
     */
    public boolean isNetSign(int userId)
        throws Throwable;
    
    /** 
     * 校验标的是否已被不良债权转让购买
     * @param loanId
     * @return
     * @throws Throwable
     */
    public boolean checkBidBadClaim(int loanId)
        throws Throwable;
    
    /** 
     * 校验用户是否购买过不良债权
     * @param userId
     * @return
     * @throws Throwable
     */
    public boolean checkIsBuyBadClaim(int userId)
        throws Throwable;
}
