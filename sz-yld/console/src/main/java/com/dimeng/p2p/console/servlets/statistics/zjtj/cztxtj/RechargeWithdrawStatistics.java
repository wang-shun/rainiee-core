package com.dimeng.p2p.console.servlets.statistics.zjtj.cztxtj;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.RechargeWithdrawManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWitReport;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWitReportStatistics;
import com.dimeng.p2p.modules.statistics.console.service.query.RecWitReportQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.SQLDateParser;

@Right(id = "P2P_C_STATISTICS_RECHARGEWITHDRAW", name = "充值提现数据统计", moduleId = "P2P_C_STATISTICS_ZJTJ_CZTX", order = 0)
public class RechargeWithdrawStatistics extends AbstractStatisticsServlet
{
    
    private static final long serialVersionUID = 1L;
    
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
        RecWitReportQuery query = new RecWitReportQuery()
        {
            @Override
            public Date getStartTime()
            {
                return SQLDateParser.parse(request.getParameter("startTime"));
            }
            
            @Override
            public Date getEndTime()
            {
                return SQLDateParser.parse(request.getParameter("endTime"));
            }
            
        };
        RechargeWithdrawManage manage = serviceSession.getService(RechargeWithdrawManage.class);
        PagingResult<RecWitReport> list = manage.getRecWitReports(query, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        RecWitReportStatistics statistics = manage.getStatistics(query);
        request.setAttribute("list", list);
        request.setAttribute("statistics", statistics);
        forwardView(request, response, getClass());
    }
}
