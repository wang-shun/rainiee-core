package com.dimeng.p2p.console.servlets.statistics.zjtj.cztxtj;

import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.RechargeWithdrawManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWitReport;
import com.dimeng.p2p.modules.statistics.console.service.query.RecWitReportQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.SQLDateParser;

@Right(id = "P2P_C_STATISTICS_RWEXPORT", name = "平台充值提现数据导出", moduleId = "P2P_C_STATISTICS_ZJTJ_CZTX")
public class RecWitStatisticsExport extends AbstractStatisticsServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int year = IntegerParser.parse(request.getParameter("year"));
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        final String startTime = request.getParameter("startTime");
        final String endTime = request.getParameter("endTime");
        final String PAGE_NUM = request.getParameter(PAGING_CURRENT);
        RecWitReportQuery query = new RecWitReportQuery()
        {
            @Override
            public Date getStartTime()
            {
                return SQLDateParser.parse(startTime);
            }
            
            @Override
            public Date getEndTime()
            {
                return SQLDateParser.parse(endTime);
            }
            
        };
        RechargeWithdrawManage manage = serviceSession.getService(RechargeWithdrawManage.class);
        PagingResult<RecWitReport> list = manage.getRecWitReports(query, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(PAGE_NUM);
            }
        });
        manage.exportStatistics(list.getItems(), response.getOutputStream(), "GBK");
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
}
