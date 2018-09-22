package com.dimeng.p2p.modules.nciic.service;

import com.dimeng.framework.service.Service;

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
}
