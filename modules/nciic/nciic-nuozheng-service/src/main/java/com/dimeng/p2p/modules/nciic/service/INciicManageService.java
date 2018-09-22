package com.dimeng.p2p.modules.nciic.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.nciic.entity.CheckBankCard;

/**
 * 实名认证查询接口.
 * 
 */
public abstract interface INciicManageService extends Service
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
    public abstract boolean check(String id, String name, String terminal, int accountId)
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
    public abstract boolean check(String id, String name, boolean duplicatedName, String terminal, int accountId)
        throws Throwable;
    
    /**
     * 银行卡号二元素验证接口
     * <功能详细描述>
     * @param name 姓名
     * @param bankCard  银行卡号码
     * @return
     * @throws Throwable
     */
    
    public abstract CheckBankCard checkBankCard(String name, String bankCard)
        throws Throwable;
}
