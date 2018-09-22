package com.dimeng.p2p.modules.statistics.console.service;

import java.util.Date;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S70.entities.T7034;
import com.dimeng.p2p.S70.entities.T7035;
import com.dimeng.p2p.S70.entities.T7052;
import com.dimeng.p2p.modules.statistics.console.service.entity.AgeDistributionEntity;
import com.dimeng.p2p.modules.statistics.console.service.entity.InvestmentLoanEntity;
import com.dimeng.p2p.modules.statistics.console.service.entity.UserMonthData;
import com.dimeng.p2p.modules.statistics.console.service.entity.UserQuarterData;

public abstract interface UserDataManage extends Service
{
    /**
     * 
     * <dt>
     * <dl>
     * 描述： 获取平台按季度统计的用户数据
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code year <= 0 } 则直接返回</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 逻辑校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>查询{@link T7034}表  查询条件{@code T7034.F01 = year}<li>
     * <li>按照{@code T7034.F02}降序排序</li>
     * 查询字段列表:
     * <ol>
     * <li>{@link UserQuarterData#quarter}对应{@code T7034.F02}</li>
     * <li>{@link UserQuarterData#userCount}对应{@code T7034.F03}</li>
     * <li>{@link UserQuarterData#newUserCount}对应{@code T7034.F04}</li>
     * <li>{@link UserQuarterData#jkUserCount}对应{@code T7034.F05}</li>
     * <li>{@link UserQuarterData#tbUserCount}对应{@code T7034.F06}</li>
     * <li>{@link UserQuarterData#jkNewUserCount}对应{@code T7034.F08}</li>
     * <li>{@link UserQuarterData#tbNewUserCount}对应{@code T7034.F09}</li>
     * 
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @return {@link UserQuarterData}{@code []} 查询结果
     * @param year 查询年份
     * @throws Throwable
     */
    public abstract UserQuarterData[] getUserQuarterData(int year)
        throws Throwable;
    
    /**
     *
     * <dt>
     * <dl>
     * 描述： 获取平台按月统计的用户数据
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code year <= 0 } 则直接返回</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 逻辑校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>查询{@link T7035}表  查询条件{@code T7035.F01 = year}<li>
     * <li>按照{@code T7035.F02}降序排序</li>
     * 查询字段列表:
     * <ol>
     * <li>{@link UserMonthData#month}对应{@code T7034.F02}</li>
     * <li>{@link UserMonthData#userCount}对应{@code T7034.F03}</li>
     * <li>{@link UserMonthData#newUserCount}对应{@code T7034.F04}</li>
     * <li>{@link UserMonthData#jkUserCount}对应{@code T7034.F05}</li>
     * <li>{@link UserMonthData#tbUserCount}对应{@code T7034.F06}</li>
     * <li>{@link UserMonthData#jkNewUserCount}对应{@code T7034.F08}</li>
     * <li>{@link UserMonthData#tbNewUserCount}对应{@code T7034.F09}</li>
     * 
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * 
     * @return {@link UserMonthData}{@code []} 查询结果
     * @param year 查询年份
     * @throws Throwable
     */
    public abstract UserMonthData[] getUserMonthData(int year)
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述： 获取已统计年限列表
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 逻辑校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>查询{@link T7035}表<li>
     * 查询字段列表:
     * <ol>
     * <li>{@code int}对应{@code DISTINCT(T7035.F01)}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * 
     * 
     * @return {@code int[]} 查询结果
     * @throws Throwable
     */
    public int[] getStatisticedYears()
        throws Throwable;
    
    /**
     * 获取全部用户的数据
     * @return
     * @throws Throwable
     */
    public abstract UserQuarterData getAllUserData()
        throws Throwable;
    
    /**
     * 投资/借款用户分布
     * @return
     * @throws Throwable
     */
    public abstract InvestmentLoanEntity getInvestmentLoanData()
        throws Throwable;
    
    /**
     * 投资/借款用户分布
     * @return
     * @throws Throwable
     */
    public abstract AgeDistributionEntity[] getAgeRanageData()
        throws Throwable;
    
    /**
     * 查询平台用户统计头部的数据
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Integer> getUserData()
        throws Throwable;
    
    /**
     * 注册用户数统计趋势图数据
     * @param type 查询类型（天、周、月、季、年）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> getRegisterUserData(String type, String startDate, String endDate)
        throws Throwable;
    
    /**
     * 平台用户数统计——统计报表
     * @param startDate
     * @param endDate
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T7052> searchUsersOpData(Paging paging, Date startDate, Date endDate)
        throws Throwable;
    
    /**
     * 平台用户数统计——统计信息
     * @param startDate
     * @param endDate
     * @return
     * @throws Throwable
     */
    public abstract T7052 searchUsersTotalData(Date startDate, Date endDate)
        throws Throwable;
    
    /**
     * 平台用户年龄分布
     * @return
     * @throws Throwable
     */
    public abstract AgeDistributionEntity[] getUserAgeData()
        throws Throwable;
    
    /**
     * 平台用户性别分布
     * @return
     * @throws Throwable
     */
    public abstract AgeDistributionEntity[] getUserSexData()
        throws Throwable;
    
    /**
     * 平台用户注册终端分布
     * @return
     * @throws Throwable
     */
    public abstract AgeDistributionEntity[] getUserRegisterSourceData()
        throws Throwable;
    
    /**
     * 平台投资终端分布
     * @return
     * @throws Throwable
     */
    public abstract AgeDistributionEntity[] getUserInvestSourceData()
        throws Throwable;
}