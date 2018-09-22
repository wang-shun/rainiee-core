package com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.blzqgmjl;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractBadClaimServlet;
import com.dimeng.p2p.repeater.claim.BadClaimManage;
import com.dimeng.p2p.repeater.claim.entity.BuyBadClaimRecord;
import com.dimeng.p2p.repeater.claim.entity.BuyBadClaimRecordQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_VIEWJGXX", name = "查看", moduleId = "P2P_C_ACCOUNT_JGXX", order = 1)
public class BlzqGmjlView extends AbstractBadClaimServlet
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
        BadClaimManage manage = serviceSession.getService(BadClaimManage.class);
        PagingResult<BuyBadClaimRecord> badClaimRecords = manage.getBuyBadClaimRecord(new BuyBadClaimRecordQuery()
        {
            @Override
            public int getUserId()
            {
                return IntegerParser.parse(request.getParameter("id"));
            }
            
            @Override
            public String getCreditNo()
            {
                return request.getParameter("creditNo");
            }
            
            @Override
            public String getLoanTitle()
            {
                return request.getParameter("loanTitle");
            }
            
            @Override
            public Timestamp getStartTime()
            {
                Date date = DateParser.parse(request.getParameter("startTime"));
                return date == null ? null : new Timestamp(date.getTime());
            }
            
            @Override
            public Timestamp getEndTime()
            {
                Date date = DateParser.parse(request.getParameter("endTime"));
                return date == null ? null : new Timestamp(date.getTime());
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
        
        request.setAttribute("badClaimRecords", badClaimRecords);
        forwardView(request, response, getClass());
    }
    
}
