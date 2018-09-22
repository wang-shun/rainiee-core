package com.dimeng.p2p.modules.base.pay.service;

import com.dimeng.framework.service.Service;

/**
 * 活动（红包、加息券）管理
 */
public interface ActivityManage extends Service
{
    
    /**
     * 加息券利息返还
     * @return 订单ID列表
     * @throws Throwable
     */
    public int[] activityInterestRtn()
        throws Throwable;

    
    /**
     * 加息券利息返还失败后回滚处理
     */
    public void changExperienceOrderToWFH(int orderId)
        throws Throwable;
}
