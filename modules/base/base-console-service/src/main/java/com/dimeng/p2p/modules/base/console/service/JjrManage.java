/**
 * 
 */
package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5128;
import com.dimeng.p2p.S51.enums.T5128_F05;

/**
 * @author tonglei
 * 
 */
public abstract interface JjrManage extends Service {
    /**
     * 
     * <dl>
     * 描述：查询节假日信息.
     * </dl>
     * 
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T5128> srarch(Paging paging) throws Throwable;

    /**
     * 查询节假日
     */
    public abstract T5128 get(int id) throws Throwable;

    /**
     * 
     * <dl>
     * 描述：录入节假日 .
     * </dl>
     * 
     * 
     * @param t5128
     * @throws Throwable
     */
    public abstract void add(T5128 t5128) throws Throwable;
    
    /**
     * 
     * <dl>
     * 描述：节假日日期验证 .
     * </dl>
     * 
     * 
     * @param t5128
     * @return boolean 是否存在
     * @throws Throwable
     */
    public abstract T5128 getHolidayByDate(T5128 t5128) throws Throwable;
    
    /**
     * 
     * <dl>
     * 描述：修改节假日 .
     * </dl>
     * 
     * 
     * @param t5128
     * @throws Throwable
     */
    public abstract void update(T5128 t5128) throws Throwable;
    
    /**
     * 
     * <dl>
     * 描述：修改节假日状态 .
     * </dl>
     * 
     * 
     * @param id
     * @param T5128_F05
     * @throws Throwable
     */
    public abstract void update(int id, T5128_F05 f05) throws Throwable;
}
