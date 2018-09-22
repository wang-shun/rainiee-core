package com.dimeng.p2p.modules.bid.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.modules.bid.console.service.entity.DfxyRecord;
import com.dimeng.p2p.modules.bid.console.service.entity.Qyjkxy;
import com.dimeng.p2p.modules.bid.console.service.entity.Zqzrxy;
import com.dimeng.p2p.modules.bid.console.service.query.DfQuery;
import com.dimeng.p2p.modules.bid.console.service.query.DzxyQuery;
import com.dimeng.p2p.modules.bid.console.service.query.ZqzrxyQuery;

/**
 * 电子协议
 * @author gaoshaolong
 *
 */
public interface PtdzxyManage extends Service
{
    
    /**
     * 电子协议
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Qyjkxy> search(DzxyQuery query, T6110_F06 f06, Paging paging)
        throws Throwable;
    
    /**
     * 债权转让电子协议
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Zqzrxy> search(ZqzrxyQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询垫付协议
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<DfxyRecord> search(DfQuery query, Paging paging)
        throws Throwable;
    
}
