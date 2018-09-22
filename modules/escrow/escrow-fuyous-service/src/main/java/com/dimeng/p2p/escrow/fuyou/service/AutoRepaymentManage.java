package com.dimeng.p2p.escrow.fuyou.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;

/**
 * 
 * 自动还款 <功能详细描述>
 * 
 * @author heshiping
 * @version [版本号, 2016年3月15日]
 */
public interface AutoRepaymentManage extends Service
{
    
    /**
     * 系统自动还款
     * @return
     * @throws Throwable
     */
    public int[] repayment(ServiceSession serviceSession)
        throws Throwable;
}
