package com.dimeng.p2p.escrow.fuyou.service;

import com.dimeng.p2p.account.user.service.SafetyManage;

/**
 *  托管引导页实名认证管理 
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年5月23日]
 */
public interface FYSafetyManage extends SafetyManage
{
    /**
     * 校验手机号是否存在
     * <功能详细描述>
     * @param phone
     * @return 
     */
    public boolean checkPhoneIsExist(String phone)
        throws Throwable;
    
    /**
     * 更新实名认证时间
     * <功能详细描述>
     * @param userId
     * @throws Throwable
     */
    public void updateT6198F06(int userId)
        throws Throwable;
    
}
