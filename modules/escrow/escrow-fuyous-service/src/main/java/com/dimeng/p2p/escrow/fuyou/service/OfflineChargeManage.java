package com.dimeng.p2p.escrow.fuyou.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;

/**
 * 
 * 线下充值
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月7日]
 */
public abstract interface OfflineChargeManage extends Service
{
    /* 
     * @param id
     *            线下充值记录ID
     * @param passed
     *            是否审核通过
     * @throws Throwable
     */
    public void exectCheck(ServiceSession serviceSession, int id)
        throws Throwable;
    
    /**
     * 线下充值订单增加
     * @param id
     * @param passed
     * @return
     * @throws Throwable
     */
    public int checkCharge(int id, boolean passed)
        throws Throwable;
}
