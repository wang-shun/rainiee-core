/*
 * 文 件 名:  RealNameStatisticsManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月5日
 */
package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.RealNameStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.RealNameStatisticsQuery;

/**
 * <实名认证统计>
 * <功能详细描述>
 * 
 * @author  God
 * @version  [版本号, 2016年4月5日]
 */
public interface RealNameStatisticsManage extends Service
{
    
    /**
     * 查询实名认证统计列表
     * <功能详细描述>
     * @param query
     * @param page
     * @return
     * @throws Throwable
     */
    PagingResult<RealNameStatisticsEntity> queryRealNameStatisticsList(RealNameStatisticsQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 导出实名认证统计列表
     * <功能详细描述>
     * @param query
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract void exportRealNameStatistics(RealNameStatisticsEntity[] entitys, OutputStream outputStream, String charset)
        throws Throwable;
}
