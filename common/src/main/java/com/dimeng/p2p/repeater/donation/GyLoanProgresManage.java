/*
 * 文 件 名:  GyLoanProgresManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.repeater.donation;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6245;
import com.dimeng.p2p.repeater.donation.entity.BidProgres;
import com.dimeng.p2p.repeater.donation.entity.GyLoan;
import com.dimeng.p2p.repeater.donation.query.ProgresQuery;

import java.math.BigDecimal;

/**
 * 公益标进展管理
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public interface GyLoanProgresManage extends Service
{
    /**
     * 添加进展
     * <功能详细描述>
     * @param t6245
     * @return
     * @throws Throwable
     */
    public int add(T6245 t6245)
        throws Throwable;
    
    /**
     * 修改公益标进展
     * <功能详细描述>
     * @param t6245
     * @return
     * @throws Throwable
     */
    public int update(T6245 t6245)
        throws Throwable;
    
    /**
     * 删除进展
     * <功能详细描述>
     * @param F01
     * @param LoanId
     * @return
     * @throws Throwable
     */
    public int delete(int F01, int LoanId)
        throws Throwable;
    
    /**
     * 根据ID获取公益标进展信息
     * <功能详细描述>
     * @param F01
     * @return
     * @throws Throwable
     */
    public T6245 get(int F01)
        throws Throwable;
    
    /**
     * 根据条件查询进展信息
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<BidProgres> search(ProgresQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 根据条件查询进展信息(前台使用)
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<BidProgres> search4front(ProgresQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 根据条件查询进展管理首页信息
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<GyLoan> searchList(ProgresQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 根据条件查询进展管理首页信息筹款总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public BigDecimal searchListAmount(ProgresQuery query)
        throws Throwable;

}
