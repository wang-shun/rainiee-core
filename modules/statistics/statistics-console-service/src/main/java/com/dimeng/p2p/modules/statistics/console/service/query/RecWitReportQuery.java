package com.dimeng.p2p.modules.statistics.console.service.query;

import java.sql.Date;

public abstract interface RecWitReportQuery
{
    
    /**
     * 起始时间范围,大于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Date getStartTime();
    
    /**
     * 结束时间范围,小于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Date getEndTime();
    
}
