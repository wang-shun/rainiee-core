package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BID_BIDZF", name = "作废", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 9)
@MultipartConfig
public class BidZf extends AbstractBidServlet
{
    
    private static final long serialVersionUID = -268955794485557959L;
    
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
        BidManage bidManage = serviceSession.getService(BidManage.class);
        int userId = serviceSession.getSession().getAccountId();
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        String reason = request.getParameter("reason");
        bidManage.updateBidStatus(loanId, reason, userId);
        sendRedirect(request, response, getController().getURI(request, LoanList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        sendRedirect(request, response, getController().getURI(request, LoanList.class));
    }
}
