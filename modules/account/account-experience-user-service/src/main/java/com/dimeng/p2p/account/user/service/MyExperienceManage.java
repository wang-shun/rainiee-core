package com.dimeng.p2p.account.user.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.account.user.service.entity.HzEntity;
import com.dimeng.p2p.account.user.service.entity.MyExperience;
import com.dimeng.p2p.account.user.service.entity.MyExperienceRecod;
import com.dimeng.p2p.account.user.service.entity.TenderAccount;
import com.dimeng.p2p.account.user.service.query.TyjBackAccountQuery;

/**
 * 我的体验金接口
 */
public interface MyExperienceManage extends Service
{
    
    /**
     * 我的体验金列表
     * 
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<MyExperienceRecod> searchAll(Map<String, Object> params, Paging paging)
        throws Throwable;
    
    /**
     * 我的体验金统计信息
     * 
     * @return
     * @throws Throwable
     */
    public abstract TenderAccount censusAll()
        throws Throwable;
    
    /**
     * 我的体验金统计信息
     * 
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<MyExperience> searMyExperience(String queryType, Paging paging)
        throws Throwable;
    
    /**
     * 查询用户有效体验金金额
     * 
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal getExperienceAmount()
        throws Throwable;
    
    /**
     * 回账查询
     * 
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<HzEntity> searchHz(TyjBackAccountQuery backOffQuery, Paging paging)
        throws Throwable;
    
    /**
     * 全部待收利息
     * 
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> getInterestTot()
        throws Throwable;

    /**
     * 查找我的有效体验金
     * 创 建 人:   luoyi
     * 创 建 时 间 : 2015年4月29日 下午2:14:34
     */
    List<T6103> findMyTyjList(int accountId);
    
    /**
     * 查询某个标是否用过体验金
     * @param bidId
     * @return
     * @throws Throwable
     */
    T6103 getT6103(int bidId)throws Throwable;
    
    /**
     * 我的体验金统计信息
     * 
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<MyExperience> searMyExperienceById(Map<String, Object> params, Paging paging)
        throws Throwable;
}
