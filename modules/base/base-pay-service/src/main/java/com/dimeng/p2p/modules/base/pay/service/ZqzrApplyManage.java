package com.dimeng.p2p.modules.base.pay.service;

import com.dimeng.framework.service.Service;

/***
 * 债权转让自动下架
 */
public interface ZqzrApplyManage extends Service
{
    
    /**
     * 债权转让下架
     */
    public void zqzrInvalid()throws Throwable;

}
