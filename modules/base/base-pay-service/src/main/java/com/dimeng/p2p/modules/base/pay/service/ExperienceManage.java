package com.dimeng.p2p.modules.base.pay.service;

import com.dimeng.framework.service.Service;

/**
 * 还款管理
 */
public interface ExperienceManage extends Service
{
    
    /**
     * 体验金利息返还
     * @return 订单ID列表
     * @throws Throwable
     */
    public int[] experienceInterestRtn()
        throws Throwable;
    
    /**
     * 体验金失效管理
     */
    public void experienceAmountInvalid()
        throws Throwable;
    
    /**
     * 体验金利息返还失败后回滚处理
     */
    public void changExperienceOrderToWFH(int orderId)
        throws Throwable;
}
