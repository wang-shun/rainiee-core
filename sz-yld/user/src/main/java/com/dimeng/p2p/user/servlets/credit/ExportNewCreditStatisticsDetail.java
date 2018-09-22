package com.dimeng.p2p.user.servlets.credit;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.user.service.JktjManage;
import com.dimeng.p2p.modules.bid.user.service.entity.NewCreditList;
import com.dimeng.p2p.modules.bid.user.service.query.NewCreditListQuery;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

public class ExportNewCreditStatisticsDetail extends AbstractCreditServlet
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
        JktjManage manage = serviceSession.getService(JktjManage.class);
        response.setHeader("Content-disposition", "attachment;filename="
                + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        NewCreditListQuery query = new NewCreditListQuery()
        {
            
            @Override
            public int getYear()
            {
                return IntegerParser.parse(request.getParameter("year"));
            }
            
            @Override
            public int getMonth()
            {
                return IntegerParser.parse(request.getParameter("month"));
            }
            
        };
        PagingResult<NewCreditList> result = manage.getNewCreditList(query, new Paging()
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

        manage.export(result.getItems(), response.getOutputStream(), "");
    }
}
