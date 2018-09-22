package com.dimeng.p2p.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S66.entities.T6601;
import com.dimeng.p2p.S66.entities.T6602;
import com.dimeng.p2p.S66.entities.T6603;

import java.util.List;

/**
 * 定时任务信息service
 * @author heluzhu
 * 
 */
public interface TaskManage extends Service
{
    
    /**
     * 添加任务信息
     * @param t6601
     * @return
     */
    public int insertoInfo(T6601 t6601)
        throws Throwable;
    
    /**
     * 修改任务信息
     * @param t6601
     * @return
     */
    public void updateInfo(T6601 t6601)
        throws Throwable;

    /**
     * 修改任务执行的开始时间和结束时间
     * @param t6601
     * @return
     */
    public void updateExcuteTime(T6601 t6601)
            throws Throwable;
    
    /**
     * 删除任务信息
     * @param id
     * @return
     */
    //public Result deleteInfo(int id);
    
    /**
     * 查询任务
     * @param name
     * *@param page
     * @return
     */
    public List<T6601> queryAllInfo(String name, Paging page)
        throws Throwable;
    
    /**
     * 根据ID查询任务信息
     * @param id
     * @return
     */
    public T6601 queryById(int id)
        throws Throwable;
    
    /**
     * 插入任务执行的异常信息
     * <功能详细描述>
     * @param t6602
     * @throws Throwable
     */
    public void insertT6602(T6602 t6602)
        throws Throwable;
    
    /**
     * 分页查询定时任务
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public PagingResult<T6601> taskList(String name,Paging paging)
        throws Throwable;
    
    /**
     * 手动执行任务
     * <功能详细描述>
     * @param id
     * @return
     */
    public void handExecuteJob(int id) throws Throwable;

}
