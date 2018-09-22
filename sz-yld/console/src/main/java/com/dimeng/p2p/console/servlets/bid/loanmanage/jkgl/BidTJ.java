package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S62.enums.T6231_F29;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BID_BIDTJ", name = "推荐", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 7)
@MultipartConfig
public class BidTJ extends AbstractBidServlet
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
        try
        {
            bidManage.updateBidTJStatus(loanId, T6231_F29.S, userId);
        }
        catch (Exception e)
        {
            if (e instanceof LogicalException)
            {
                StringBuffer sb = new StringBuffer();
                sb.append(e.getMessage());
                prompt(request, response, PromptLevel.ERROR, sb.toString());
                //                forwardView(request, response, LoanList.class);
                sendRedirect(request, response, getController().getURI(request, LoanList.class));
                return;
            }
            else
            {
                onThrowable(request, response, e);
            }
        }
        sendRedirect(request, response, getController().getURI(request, LoanList.class));
    }
}
