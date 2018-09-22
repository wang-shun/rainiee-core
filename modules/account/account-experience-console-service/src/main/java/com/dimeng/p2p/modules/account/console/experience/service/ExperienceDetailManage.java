package com.dimeng.p2p.modules.account.console.experience.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceDetail;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceProfit;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalInfo;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalList;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceDetailQuery;

/**
 * 体验金详情管理
 *
 * @author guopeng
 */
public abstract interface ExperienceDetailManage extends Service {
    /**
     * 查询体验金详情
     *
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<ExperienceDetail> search(
            ExperienceDetailQuery query, Paging paging) throws Throwable;

    /**
     * 查询体验金收益详细
     *
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<ExperienceProfit> get(String id, int userId, Paging paging)
            throws Throwable;

    /**
     * 体验金详情导出
     *
     * @param outputStream
     * @throws Throwable
     */
    public abstract void export(ExperienceDetail[] experienceDetails,
                                OutputStream outputStream) throws Throwable;

    /**
     * 体验金详情列表-统计
     * @return ExperienceTotalInfo
     * @throws Throwable
     */
    public ExperienceTotalInfo getExperienceTotal() throws Throwable;

    /**
     * 体验金详情列表
     * @param query 查询条件
     * @param paging 分页
     * @return PagingResult
     * @throws Throwable
     */
    public PagingResult<ExperienceTotalList> searchTotalList(ExperienceDetailQuery query, Paging paging) throws Throwable;
    
    /**
     * 体验金详情列表投资总金额,总体验金,总收益
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public ExperienceTotalList searchTotalAmount(ExperienceDetailQuery query)
        throws Throwable;

    /**
     * 体验金详情列表-导出
     * @param query 查询条件
     * @return ExperienceTotalList
     * @throws Throwable
     */
    public ExperienceTotalList[] exportsExperienceLists(ExperienceDetailQuery query) throws Throwable;


}
