package com.dimeng.p2p.modules.nciic.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6141;

/**
 * 实名认证查询接口.
 * 
 */
public interface INciicManageService extends Service
{
    
    /**
     * 实名认证.
     * 
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @param terminal 终端类型：PC、APP、WX
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    boolean check(String id, String name, String terminal, int accountId)
        throws Throwable;
    
    /**
     * 实名认证.
     * 
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @param duplicatedName
     *            是否允许重名
     * @param terminal 终端类型：PC、APP、WX
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    boolean check(String id, String name, boolean duplicatedName, String terminal, int accountId)
        throws Throwable;
    
    /**
    * 修改实名认证
    * @param name
    * @throws Throwable
    */
    public abstract void updateName(String name, String idcard, String status)
        throws Throwable;
    
    /**
     * 根据身份证号码查询记录
     * <功能详细描述>
     * @param idcard
     * @return
     */
    public abstract T6141 selectT6141(String idcard)
        throws Throwable;
    
}
