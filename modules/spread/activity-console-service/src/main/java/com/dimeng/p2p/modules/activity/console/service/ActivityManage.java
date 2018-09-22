package com.dimeng.p2p.modules.activity.console.service;

import java.io.InputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6340;
import com.dimeng.p2p.S63.entities.T6344;
import com.dimeng.p2p.modules.activity.console.service.entity.Activity;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityList;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityLog;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityQuery;

/**
 * 
 * 活动管理
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月9日]
 */
public abstract interface ActivityManage extends Service
{
    
    /**
     * <dt>
     * <dl>
     * 描述：分页查询活动列表
     * </dl>
     * 
     * @param query 查询条件
     * @param paging 分页参数
     * @return {@link PagingResult}{@code <}{@link ActivityList}{@code >} 分页查询结果,没有结果则返回{@code null}
     * @throws Throwable
     */
    public PagingResult<ActivityList> searchActivity(ActivityQuery query, Paging paging)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：添加活动信息
     * </dl>
     * @param activity 活动信息
     * @throws Throwable
     */
    public void addActivity(Activity activity)
        throws Throwable;
    
    /**
     * 查询单个活动
     * <功能详细描述>
     * @param id
     * @return
     * @throws Throwable
     */
    public T6340 getActivity(int id)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据id修改活动信息.
     * </dl>
     * 
     * @param id 活动ID
     * @param activity 活动信息
     * @throws Throwable
     */
    public void updateActivity(int id, Activity activity)
        throws Throwable;
    
    /**
     * 修改活动状态
     * @param id
     * @param status
     * @throws Throwable
     */
    public void updateStatus(int id, String status)
        throws Throwable;
    
    /**
     * 导入用户
     * @param inputStream
     * @param charset
     * @return
     * @throws Throwable
     */
    public String[] importUsers(InputStream inputStream, String charset)
        throws Throwable;
    
    /**
     * 根据活动查询所有活动操作日志
     * <功能详细描述>
     * @param actId
     * @return
     * @throws Throwable
     */
    public ActivityLog[] activityLogs(int actId)
        throws Throwable;
    
    /**
     * 查询是否存在预上架、进行中的活动
     * <功能详细描述>
     * @param jlType 奖励类型
     * @param hdType 活动类型
     * @return
     * @throws Throwable
     */
    public String queryExistNo(String jlType, String hdType)
        throws Throwable;
    
    /**
     * 查询指定用户赠送活动的用户名
     * <功能详细描述>
     * @param actId
     * @return
     * @throws Throwable
     */
    public String queryForUsers(int actId)
        throws Throwable;

    /**
     * 查询指定用户赠送体验金的用户名
     * <功能详细描述>
     * @param actId
     * @return
     * @throws Throwable
     */
    String queryExperienceForUsers(int actId) throws Throwable;

    /**
     * 查询指定活动的所有规则
     * @param actId
     * @return
     * @throws Throwable
     */
    public T6344[] getT6344Arry(int actId) throws Throwable;
}
