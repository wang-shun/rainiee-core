package com.dimeng.p2p.modules.bid.user.service;

import com.dimeng.framework.service.Service;

public abstract interface ZqzrSignaManage extends Service
{
    
    /**
     * 根据债权转让订单ID生成债权转让合同
     * <功能详细描述>
     * @param orderId 转让订单ID
     * @throws Throwable
     */
    public void setZqzrSigna(int orderId)
        throws Throwable;
    
    /**
     * 根据债权转让ID生成债权转让合同
     * <功能详细描述>
     * @param zqzrId 债权转让申请ID
     * @throws Throwable
     */
    public void setZqzrSignaZqzrId(int zqzrId)
        throws Throwable;
    
}
