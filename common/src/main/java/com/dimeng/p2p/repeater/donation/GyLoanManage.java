package com.dimeng.p2p.repeater.donation;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6243;
import com.dimeng.p2p.S62.entities.T6246;
import com.dimeng.p2p.S62.entities.T6247;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.repeater.donation.entity.Article;
import com.dimeng.p2p.repeater.donation.entity.Donation;
import com.dimeng.p2p.repeater.donation.entity.GyBidCheck;
import com.dimeng.p2p.repeater.donation.entity.GyLoan;
import com.dimeng.p2p.repeater.donation.entity.GyLoanStatis;
import com.dimeng.p2p.repeater.donation.query.DonationQuery;
import com.dimeng.p2p.repeater.donation.query.GyLoanQuery;

/**
 * 公益标管理
 * <功能详细描述>
 * 
 * @author  liuguangwen
 * @version  [版本号, 2015年3月9日]
 */
public abstract interface GyLoanManage extends Service
{
    /**
     * 添加公益标信息
     * <功能详细描述>
     * @param t6242
     * @return
     * @throws Throwable
     */
    public int add(T6242 t6242)
        throws Throwable;
    
    /**
     * 添加公益标扩展信息
     * <功能详细描述>
     * @param t6242
     * @return
     * @throws Throwable
     */
    public void addT6243(T6243 t6243, Article article)
        throws Throwable;
    
    /**
     * 添加公益标投资记录
     * <功能详细描述>
     * @param t6246
     * @return
     * @throws Throwable
     */
    public void addT6246(T6246 t6246)
        throws Throwable;
    
    /**
     * 添加公益标审核信息
     * <功能详细描述>
     * @param t6246
     * @return
     * @throws Throwable
     */
    public void addT6247(T6247 t6247)
        throws Throwable;
    
    /**
     * 修改公益标信息
     * <功能详细描述>
     * @param t6242
     * @return
     * @throws Throwable
     */
    public int update(T6242 t6242)
        throws Throwable;
    
    /**
     * 修改公益标倡议书的内容
     * <功能详细描述>
     * @param t6243
     * @return
     * @throws Throwable
     */
    public void updateT6243(T6243 t6243, Article article)
        throws Throwable;
    
    /**
     * 删除公益标信息
     * <功能详细描述>
     * @param t6242
     * @return
     * @throws Throwable
     */
    public void delete(T6242 t6242)
        throws Throwable;
    
    /**
     * 获取公益标信息
     * <功能详细描述>
     * @param t6242
     * @return
     * @throws Throwable
     */
    public T6242 get(int F01)
        throws Throwable;
    
    /**
     * 获取公益标扩展信息信息
     * <功能详细描述>
     * @param F01:标ID
     * @return
     * @throws Throwable
     */
    public T6243 getT6243(int F01)
        throws Throwable;
    
    /**
     * 获取公益标信息
     * 包括: 标扩展信息,标的协议信息
     * <功能详细描述>
     * @param F01
     * @return
     * @throws Throwable
     */
    public GyLoan getInfo(int F01)
        throws Throwable;
    
    /**
     * 查询公益标列表,分页
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<GyLoan> search(GyLoanQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询公益标列表筹款总金额、已筹款总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public T6242 searchAmoun(GyLoanQuery query)
        throws Throwable;
    
    /**
     * 查询公益标列表,分页(前台专用)
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<GyLoan> search4front(GyLoanQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询公益标投资记录
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<Donation> searchTbjl(DonationQuery query, Paging paging)
        throws Throwable;
    
    /**
     * <平台帐号ID>
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public int getPTID()
        throws Throwable;
    
    /**
     * <公益标的类型>
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public T6211[] getBidType()
        throws Throwable;
    
    /**
     * <公益标的审核记录>
     * <功能详细描述>
     * @param bid
     * @return
     * @throws Throwable
     */
    public GyBidCheck[] getGyBidCheck(int bid)
        throws Throwable;
    
    /**
     * <公益标审核>
     * 通过,不通过,作废
     * <功能详细描述>
     * status: 状态
     * oldStatus: 旧状态
     */
    public void CheckGyBid(int bid, T6242_F11 status, T6242_F11 oldStatus)
        throws Throwable;
    
    /**
     * <统计公益标相关>
     * <功能详细描述>
     * 如果不传ID,总计所有数量,
     * 如果传ID,统计ID公益的相关数量
     * @param F01 公益标ID
     * @throws Throwable
     */
    public GyLoanStatis gyLoanStatistics(int F01)
        throws Throwable;
    
    /**
     * <统计公益标个人捐款相关>
     * <功能详细描述>
     * @param userId 用户ID
     * @throws Throwable
     */
    public GyLoanStatis gyLoanStatisticsByUid(int userId)
        throws Throwable;
    
    /**
     * <导出公益标信息>
     * <功能详细描述>
     * @param loans
     * @param outputStream
     * @param charset UTF-8
     * @throws Throwable
     */
    public void exportGyLoan(GyLoan[] loans, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <导出公益标信息>
     * <会统计公益标进展数>
     * @param loans
     * @param outputStream
     * @param charset UTF-8
     * @throws Throwable
     */
    public void exportGyLoanProgers(GyLoan[] loans, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <获取公益标协议内容>
     * <功能详细描述>
     * @param loanId
     * @return
     * @throws Throwable
     */
    public Dzxy getBidContent(int loanId)
        throws Throwable;
}
