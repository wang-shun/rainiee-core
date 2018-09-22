package com.dimeng.p2p.escrow.fuyou.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.escrow.fuyou.entity.console.T6110_FY;

public abstract interface UserAcctQueryManage extends Service
{
    
    /**
    * 获取用户列表
    * 
    * @return
    * @throws Throwable
    */
    public PagingResult<T6110_FY> selectUserList(ServiceSession serviceSession, String name, Paging paging, String userTag)
        throws Throwable;
    
}
