/*
 * 文 件 名:  ExportRealNameStatistics.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月5日
 */
package com.dimeng.p2p.console.servlets.statistics.tjbb.smrztj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.statistics.console.service.RealNameStatisticsManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.RealNameStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.RealNameStatisticsQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * <导出实名认证统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年4月5日]
 */
@Right(id = "P2P_C_STATISTICS_EXPORTREALNAME", name = "导出", moduleId = "P2P_C_STATISTICS_TJBB_SMRZ")
public class ExportRealNameStatistics extends AbstractFinanceServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -4388831999738174839L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        RealNameStatisticsManage manage = serviceSession.getService(RealNameStatisticsManage.class);
        RealNameStatisticsQuery query = new RealNameStatisticsQuery()
        {
            
            @Override
            public String getUserName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public String getRealName()
            {
                return request.getParameter("realName");
            }
            
            @Override
            public String getAuthSource()
            {
                return request.getParameter("authSource");
            }
            
            @Override
            public Timestamp getAuthPassTimeStart()
            {
                return TimestampParser.parse(request.getParameter("authPassTimeStart"));
            }
            
            @Override
            public Timestamp getAuthPassTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("authPassTimeEnd"));
            }
            
        };
        PagingResult<RealNameStatisticsEntity> result = manage.queryRealNameStatisticsList(query, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        
        manage.exportRealNameStatistics(result.getItems(), response.getOutputStream(), "");
    }
}
