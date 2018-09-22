package com.dimeng.p2p.console.servlets.statistics.tjbb.dftj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.DFStatisticsManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.DFStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.DFStatisticsQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_STATISTICS_JGDF", name = "机构垫付统计表", moduleId = "P2P_C_STATISTICS_TJBB_JGDFTJ", order = 0)
public class DFStatistics extends AbstractStatisticsServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        DFStatisticsManage dfManage = serviceSession.getService(DFStatisticsManage.class);
        DFStatisticsQuery query = new DFStatisticsQuery()
        {
            
            @Override
            public Timestamp getReMoneyTimeStart()
            {
                return TimestampParser.parse(request.getParameter("reMoneyTimeStart"));
            }
            
            @Override
            public Timestamp getReMoneyTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("reMoneyTimeEnd"));
            }
            
            @Override
            public String getLoanId()
            {
                return request.getParameter("loanId");
            }
            
            @Override
            public Timestamp getDfTimeStart()
            {
                return TimestampParser.parse(request.getParameter("dfTimeStart"));
            }
            
            @Override
            public Timestamp getDfTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("dfTimeEnd"));
            }
            
            @Override
            public String getDfAccountName()
            {
                return request.getParameter("dfAccountName");
            }
            
            @Override
            public String getDfAccount()
            {
                return request.getParameter("dfAccount");
            }

            @Override
            public String getJKAccount() {
                return request.getParameter("jkAccount");
            }
        };
        PagingResult<DFStatisticsEntity> result = dfManage.getDFList(query, new Paging()
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
        request.setAttribute("result", result);
        request.setAttribute("dfTotal", dfManage.getDFTotal(query));
        forwardView(request, response, getClass());
    }
}
