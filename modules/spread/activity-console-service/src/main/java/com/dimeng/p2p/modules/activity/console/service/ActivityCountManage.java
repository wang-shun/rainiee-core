/*
 * 文 件 名:  InterestRateManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月8日
 */
package com.dimeng.p2p.modules.activity.console.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityCountEntity;
import com.dimeng.p2p.modules.activity.console.service.entity.HbAmountTotalInfo;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqClearAmountTotalInfo;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqClearCountEntity;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqCountTotalInfo;
import com.dimeng.p2p.modules.activity.console.service.query.ActivityCountQuery;
import com.dimeng.p2p.modules.activity.console.service.query.JxqClearCountQuery;

/**
 * 功能描述:红包统计,加息券统计，加息券结算统计
 * 详细描述： 1. 按照条件查询红包列表，加息券列表，加息券结算列表
 *        2. 统计已使用红包金额和未使用红包金额
 *        3. 统计已使用加息券个数和未使用加息券个数
 *        4. 统计待付加息券奖励金额和已付加息券奖励金额
 *        5. 导出红包统计列表，加息券统计列表，加息券结算统计列表
 * @author  xiaoqi
 * @version  [版本号, 2015年10月8日]
 */
public abstract interface ActivityCountManage extends Service
{
    /**
     * 获取加息券/红包 统计列表
     * 根据查询条件分页展示 加息券/红包 列表
     * @param activityCountQuery 查询条件参数集合
     * @param paging 分页参数
     * @param activityType 活动类型[redpacket：红包；interest：加息卷]
     * @return 分页实体
     * @throws Throwable
     */
    public abstract PagingResult<ActivityCountEntity> searchActivityCount(ActivityCountQuery activityCountQuery,
        Paging paging, String activityType)
        throws Throwable;

    /**
     * 获取体验金统计列表
     * 根据查询条件分页展示体验金列表
     * @param activityCountQuery 查询条件参数集合
     * @param paging 分页参数
     * @param activityType 活动类型[redpacket：红包；interest：加息卷; experience: 体验金]
     * @return 分页实体
     * @throws Throwable
     */
    PagingResult<ActivityCountEntity> searchActivityExpCount(ActivityCountQuery activityCountQuery, Paging paging, String activityType) throws Throwable;
    
    /**
     * 分别 统计加息券 [未使用/已使用] 的个数
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract JxqCountTotalInfo getJxqCountTotalInfo()
        throws Throwable;
    
    /**
     * 分别 统计红包 [未使用/已使用]金额 
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract HbAmountTotalInfo getHbAmountTotalInfo()
        throws Throwable;
    
    /**
     * 统计根据条件查询的红包金额总计
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal getConditionOfHbAmount(ActivityCountQuery query, String activityType)
        throws Throwable;
    
    /**
     * 导出 根据条件查询出的 [加息券/红包] 统计列表数据
     * <功能详细描述>
     * @param activityCountQuery 查询条件参数集合
     * @param activityType 活动类型[redpacket：红包；interest：加息卷]
     * @return 
     * @throws Throwable
     */
    public abstract ActivityCountEntity[] exportActivityCountList(ActivityCountQuery activityCountQuery,
        String activityType)
        throws Throwable;

    /**
     * 导出 根据条件查询出的 [体验金] 统计列表数据
     * <功能详细描述>
     * @param activityCountQuery 查询条件参数集合
     * @param activityType 活动类型[experience：体验金]
     * @return
     * @throws Throwable
     */
    ActivityCountEntity[] exportActivityExpCountList(ActivityCountQuery activityCountQuery,
                                                                  String activityType)
            throws Throwable;
    
    /**
     * 根据查选条件分页查询出加息券结算[待付/已付]列表数据
     * <功能详细描述>
     * @param query 查询条件参数
     * @param paging 分页参数
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<JxqClearCountEntity> searchJxqClearCount(JxqClearCountQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 统计加息券待付/已付 加息奖励金额总计
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract JxqClearAmountTotalInfo getJxqClearAmountTotalInfo()
        throws Throwable;
    
    /**
     * 统计根据条件查询出的加息券结算金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal getConditionOfJxqClearAmount(JxqClearCountQuery query)
        throws Throwable;
    
    /**
     * 默认导出全部的[待付/已付]加息券结算列表
     * 根据条件导出[待付/已付]加息券结算列表
     * @param query 查询条件
     * @param paging 分页参数
     * @return
     * @throws Throwable
     */
    public abstract JxqClearCountEntity[] exportJxqClearCountList(JxqClearCountQuery query)
        throws Throwable;
    
}
