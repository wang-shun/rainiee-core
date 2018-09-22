package com.dimeng.p2p.modules.account.console.experience.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceStaticTotal;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceStatistics;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceStatisticsQuery;

/**
 * 体验金统计
 * 
 * @author guopeng
 * 
 */
public abstract interface ExperienceStatisticsManage extends Service {
	/**
	 * 体验金统计查询近30天待还
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<ExperienceStatistics> searchDh(
			ExperienceStatisticsQuery query, Paging paging) throws Throwable;
    
    /**
     * 体验金统计查询近30天待还体验总金额，应还总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract ExperienceStatistics searchDhAmount(ExperienceStatisticsQuery query)
        throws Throwable;

	/**
	 * 体验金统计查询已还
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<ExperienceStatistics> searchYh(
			ExperienceStatisticsQuery query, Paging paging) throws Throwable;
    
    /**
     * 体验金统计查询已还体验总金额，应还总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract ExperienceStatistics searchYhAmount(ExperienceStatisticsQuery query)
        throws Throwable;

    /**
     * 体验金详情列表-统计
     * @return ExperienceTotalInfo
     * @throws Throwable
     */
    public ExperienceStaticTotal getExperienceTotal() throws Throwable;

	/**
	 * 体验金统计-待还-导出
	 * 
	 * @param outputStream
	 * @throws Throwable
	 */
	public abstract void exportexportDH(ExperienceStatistics[] experienceStatistics,
			OutputStream outputStream) throws Throwable;

    /**
     * 体验金统计-已还-导出
     * @param outputStream
     * @throws Throwable
     */
    public abstract void exportexportYH(ExperienceStatistics[] experienceStatistics,
                                        OutputStream outputStream) throws Throwable;
}
