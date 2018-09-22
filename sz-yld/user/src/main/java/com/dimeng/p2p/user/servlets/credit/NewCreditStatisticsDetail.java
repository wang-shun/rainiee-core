package com.dimeng.p2p.user.servlets.credit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.user.service.JktjManage;
import com.dimeng.p2p.modules.bid.user.service.entity.NewCreditList;
import com.dimeng.p2p.modules.bid.user.service.query.NewCreditListQuery;
import com.dimeng.util.parser.IntegerParser;

public class NewCreditStatisticsDetail extends AbstractCreditServlet
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
                return 12;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        request.setAttribute("result", result);
        request.setAttribute("creditTotal", manage.getNewCreditTotal(query));
        forwardView(request, response, getClass());
    }
}
