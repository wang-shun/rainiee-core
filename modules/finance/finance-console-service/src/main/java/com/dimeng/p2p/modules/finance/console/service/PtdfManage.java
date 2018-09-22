/*
 * 文 件 名:  CollectionManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  dingjinqing
 * 修改时间:  2015年3月12日
 */
package com.dimeng.p2p.modules.finance.console.service;

import java.io.OutputStream;
import java.sql.SQLException;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.modules.finance.console.service.entity.DfRecord;
import com.dimeng.p2p.modules.finance.console.service.query.DfQuery;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  dingjinqing
 * @version  [版本号, 2015年3月12日]
 */
public interface PtdfManage extends Service
{
    /** 查询逾期待垫付记录
     * <功能详细描述>
     * @param yqddfQuery
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<DfRecord> yqddfSearch(DfQuery yqddfQuery, Paging paging)
        throws Throwable;
    
    /**
     * 查询逾期待垫付记录借款总金额，应还总本金，应还总利息，逾期总罚息
     * <功能详细描述>
     * @param yqddfQuery
     * @return
     * @throws Throwable
     */
    public abstract DfRecord yqddfSearchAmount(DfQuery yqddfQuery)
        throws Throwable;

    /** 查询已垫付记录
     * <功能详细描述>
     * @param yqddfQuery
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<DfRecord> ydfSearch(DfQuery yqddfQuery, Paging paging)
        throws Throwable;
    
    /**
     * 查询已垫付记录借款总金额，应还总本金，应还总利息，逾期总罚息,垫付总金额，垫付返还总金额
     * <功能详细描述>
     * @param yqddfQuery
     * @return
     * @throws Throwable
     */
    public abstract DfRecord ydfSearchAmount(DfQuery yqddfQuery)
        throws Throwable;

    /** 查看垫付已结清
     * <功能详细描述>
     * @param paging
     * @return
     * @throws Throwable 
     */
    public abstract PagingResult<DfRecord> dfyjqSearch(DfQuery yqddfQuery, Paging paging)
        throws Throwable;
    
    /**
     * 查看垫付已结清借款总金额，应还总本金，应还总利息，逾期总罚息,垫付总金额，垫付返还总金额
     * <功能详细描述>
     * @param yqddfQuery
     * @return
     * @throws Throwable
     */
    public abstract DfRecord dfyjqSearchAmount(DfQuery yqddfQuery)
        throws Throwable;

    /** <到处逾期垫付>
     * <功能详细描述>
     * @param items
     * @param outputStream
     */
    public abstract void exportYqddf(DfRecord[] items, OutputStream outputStream, String charset)
        throws Throwable;
    
    /** <到处已垫付>
     * <功能详细描述>
     * @param items
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportYdf(DfRecord[] items, OutputStream outputStream, String charset)
        throws Throwable;
    
    /** <导出垫付返还>
     * <功能详细描述>
     * @param items
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportDffh(DfRecord[] items, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
	 * 查询平台垫付类型
	 */
    public T5131_F02 selectT5131() throws SQLException;
}
