package com.dimeng.p2p.modules.base.console.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6290;
import com.dimeng.p2p.S62.enums.T6290_F04;
import com.dimeng.p2p.S62.enums.T6290_F06;
import com.dimeng.p2p.common.entities.BillRemindSet;

/**
 * 还款提醒设置管理
 * 
 */
public abstract interface RepaymentManage extends Service
{
    /**
     * 新增还款提醒设置信息
     * 
     * @param t6216
     * @throws Throwable
     */
    public abstract int add(T6290 t6290)
        throws Throwable;
    
    /**
     * 修改还款提醒设置信息
     * 
     * @param bankId
     * @param bank
     * @throws Throwable
     */
    public abstract void update(T6290 t6290)
        throws Throwable;
    
    /**
     * 根据ID得到还款提醒设置信息
     * 
     * @param productId
     * @return
     * @throws Throwable
     */
    /*public abstract T6290 get(int repaymentId)
        throws Throwable;*/
    
    /**
     * 根据ID、修改还款提醒设置状态启用
     * 
     * @param productId
     * @throws Throwable
     */
    public abstract void enable(int repaymentId)
        throws Throwable;
    
    /**
     * 根据ID、修改还款提醒设置状态停用
     * 
     * @param productId
     * @throws Throwable
     */
    public abstract void disable(int repaymentId)
        throws Throwable;
    
    /**
     * 判断产品是否存在
     * 
     * @param name
     * @throws Throwable
     */
    /*
    public abstract T6290 getValidRepaymentSet()
     throws Throwable;*/
    
    /**
     * 判断产品是否存在
     * 
     * @param name
     * @throws Throwable
     */
    /*
    public abstract T6290 getRepaymentSet()
     throws Throwable;*/
    
    public abstract T6252[] selectT6252s(String condition)
        throws Throwable;
    
    /**
    * 添加站内信
    * @param userId
    * @param title
    * @param timestamp
    * @return
    * @throws Throwable
    */
    public abstract int addT6123(int userId, String title, Timestamp timestamp)
        throws Throwable;
    
    public abstract void addT6124(int letterId, String title)
        throws Throwable;
    
    public abstract int getPassedCount(Date date)
        throws Throwable;
    
    /**
     * <账单提醒设置>
     * @param F04
     * @param F06
     * @return List<T6290>
     */
    public abstract List<T6290> getT6290List(T6290_F04 F04, T6290_F06 F06)
        throws Throwable;
    
    /**
     * 修改还款提醒设置信息
     * 
     * @param billRemindSet
     * @throws Throwable
     */
    public abstract void updateT6290(BillRemindSet billRemindSet)
        throws Throwable;
}
