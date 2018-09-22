package com.dimeng.p2p.repeater.business;

import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.entities.T7190;
import com.dimeng.p2p.repeater.business.entity.BusinessOptLog;
import com.dimeng.p2p.repeater.business.entity.CustomerEntity;
import com.dimeng.p2p.repeater.business.entity.Performance;
import com.dimeng.p2p.repeater.business.entity.SysUser;
import com.dimeng.p2p.repeater.business.query.BusinessUserQuery;
import com.dimeng.p2p.repeater.business.query.CustomerQuery;
import com.dimeng.p2p.repeater.business.query.PerformanceQuery;
import com.dimeng.p2p.repeater.business.query.ResultsQuery;
import com.dimeng.p2p.repeater.business.query.SysUserQuery;

/**
 * 
 * 业务员
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月9日]
 */
public abstract interface SysBusinessManage extends Service
{
    
    /**
     * 获取最大的工号
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract int getMaxEmployNum()
        throws Throwable;
    
    /**
     * 查询业务员
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract List<SysUser> getEmployNumUsers(SysUserQuery query)
        throws Throwable;
    
    /**
     * 分页查询业务员
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<SysUser> seachBusinessUser(BusinessUserQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 
     * 
     * 描述：查询业务员业绩统计情况
     * @param 
     * @return 
     * @throws
     */
    public abstract Performance findPerformance(PerformanceQuery query)
        throws Throwable;
    
    /**
     * 
     * 
     * 描述：统计业务员业绩
     * @param 
     * @return 
     * @throws
     */
    public abstract Performance findPerformances()
        throws Throwable;
    
    /**
     * 查询业务员业绩统计情况(投资借款记录)
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T7190> serarchTbjl(PerformanceQuery query, Paging paging)
        throws Throwable;
    
    /**
       * 统计管理 查询业务员业绩统计情况(投资借款记录)
       * @param paging
       * @return
       * @throws Throwable
       */
    public abstract PagingResult<T7190> serarchTjgl(PerformanceQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询下拉列表中的业务员
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract SysUser[] getEmployUsers(BusinessUserQuery query)
        throws Throwable;
    
    /**
     * 查询业务员的锁定日志
     * <功能详细描述>
     * @param employNum
     * @return
     * @throws Throwable
     */
    public abstract BusinessOptLog[] searchLogs(String employNum)
        throws Throwable;
    
    /**
     * 查询业务员业绩明细(统计管理)
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> searchAll(ResultsQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询业务员名下的所有客户信息
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<CustomerEntity> selectCustomers(CustomerQuery query, Paging paging)
        throws Throwable;
    
}
