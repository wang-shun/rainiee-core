package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.AnnexManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.parser.IntegerParser;

public class DelAnnexWz extends AbstractBidServlet
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
        AnnexManage annexManage = serviceSession.getService(AnnexManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        BidManage bidManage = serviceSession.getService(BidManage.class);
        T6230 t6230 = bidManage.get(loanId);
        if (t6230 != null && t6230.F20 != T6230_F20.SQZ)
        {
            prompt(request, response, PromptLevel.ERROR, "不是申请中状态");
            sendRedirect(request, response, getController().getURI(request, LoanList.class));
            return;
        }
        annexManage.delFgk(id);
        sendRedirect(request, response, getController().getURI(request, AddAnnexWz.class) + "?loanId=" + loanId
            + "&userId=" + userId);
    }
}
