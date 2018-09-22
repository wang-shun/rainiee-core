package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.DFStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.DFStatisticsQuery;

public abstract interface DFStatisticsManage extends Service
{
    
    public abstract PagingResult<DFStatisticsEntity> getDFList(DFStatisticsQuery query, Paging paging)
        throws Throwable;
    
    public abstract void export(DFStatisticsEntity[] recWits, OutputStream outputStream, String charset)
        throws Throwable;
    
    public abstract Map<String, String> getDFTotal(DFStatisticsQuery query)
        throws Throwable;

    public abstract PagingResult<DFStatisticsEntity> getPTDFList(DFStatisticsQuery query, Paging paging)
            throws Throwable;

    public abstract Map<String, String> getPTDFTotal(DFStatisticsQuery query)
            throws Throwable;

    public abstract void exportPtdf(DFStatisticsEntity[] recWits, OutputStream outputStream, String charset)
            throws Throwable;
}
