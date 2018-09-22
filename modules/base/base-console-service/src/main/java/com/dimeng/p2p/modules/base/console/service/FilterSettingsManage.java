package com.dimeng.p2p.modules.base.console.service;

import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6299;
import com.dimeng.p2p.S62.enums.T6299_F04;

/**
 * 筛选条件设置
 * 
 */
public abstract interface FilterSettingsManage extends Service {
    
    /** 获取第一条筛选条件
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract T6299 getFirst(T6299_F04 type)
        throws Throwable;
    
    /** 获取最后一条筛选条件
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract T6299 getLast(T6299_F04 type)
        throws Throwable;
    
    /**
     * 获取增加的筛选条件集合
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract T6299[] getAddFilter(T6299_F04 type)
        throws Throwable;
    
    /** 更新筛选条件
     * <功能详细描述>
     * @param updateList
     * @throws Throwable
     */
    public abstract void updateFilterSettings(List<T6299> updateList)
        throws Throwable;
    
    /**
     * 增加筛选条件
     * <功能详细描述>
     * @param addList
     * @throws Throwable
     */
    public abstract void addFilterSettings(List<T6299> addList)
        throws Throwable;
    
    /** 根据id删除指定的筛选条件
     * <功能详细描述>
     * @param id
     * @throws Throwable
     */
    public abstract void delFilterSettings(int id)
        throws Throwable;

}
