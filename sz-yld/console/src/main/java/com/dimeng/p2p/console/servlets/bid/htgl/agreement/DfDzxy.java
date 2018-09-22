package com.dimeng.p2p.console.servlets.bid.htgl.agreement;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.PtdzxyManage;
import com.dimeng.p2p.modules.bid.console.service.entity.DfxyRecord;
import com.dimeng.p2p.modules.bid.console.service.query.DfQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_BID_HTGL_DZXY", isMenu = true, name = "平台电子协议", moduleId = "P2P_C_BID_HTGL_PTDZXY", order = 0)
public class DfDzxy extends AbstractBidServlet
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
        PtdzxyManage ptdzxyManage = serviceSession.getService(PtdzxyManage.class);
        PagingResult<DfxyRecord> result = ptdzxyManage.search(new DfQuery()
        {
            @Override
            public String getName()
            {
                return request.getParameter("name");
            }
            
            @Override
            public Timestamp getAdvanceTimeStart()
            {
                return TimestampParser.parse(request.getParameter("advanceTimeStart"));
            }
            
            @Override
            public Timestamp getAdvanceTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("advanceTimeEnd"));
            }
            
        }, new Paging()
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
        forwardView(request, response, getClass());
    }
}
