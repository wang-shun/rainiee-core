package com.dimeng.p2p.modules.bid.user.service;

import java.sql.Connection;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.bid.user.service.entity.AutoBidSet;
import com.dimeng.p2p.modules.bid.user.service.query.AutoBidQuery;

/**
 * 自动理财工具接口
 *
 */
public abstract interface ZdtbManage extends Service
{
    
    /**
     * 保存投资设置
     * @throws Throwable
     */
    public abstract void save(AutoBidQuery autoBidQuery)
        throws Throwable;
    
    /**
     * 统计自动投资规则总条数
     * @throws Throwable
     */
    public abstract int autoBidCount()
        throws Throwable;
    
    /**
     * 统计状态为“开启”的自动投资规则总条数
     * @throws Throwable
     */
    public abstract int autoBidCountQY(Connection conn)
        throws Throwable;
    
    /**
     * 保存投资设置但不开启
     * @throws Throwable
     */
    public abstract int onlySave(AutoBidQuery autoBidQuery)
        throws Throwable;
    
    /**
     * 更改自动投资规则状态
     * @throws Throwable
     */
    public abstract void updateStatus(String status, int id)
        throws Throwable;
    
    /**
     * 查询自动投资信息
     * @return
     * @throws Throwable
     */
    public abstract List<AutoBidSet> search()
        throws Throwable;
    
    /**
     * 根据借款期间查询是否存在相同的规则
     * <功能详细描述>
     * @param start
     * @return
     * @throws Throwable
     */
    public abstract int queryExist(int start, int end)
        throws Throwable;
    
    /**
     * 删除自动投资规则
     * <功能详细描述>
     * @param id
     * @throws Throwable
     */
    public abstract void delete(int id)
        throws Throwable;
    
}
