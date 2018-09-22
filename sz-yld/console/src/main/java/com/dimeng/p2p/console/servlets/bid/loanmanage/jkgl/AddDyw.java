package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6235;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BID_ADDPROJECT", name = "新增", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 1)
public class AddDyw extends AbstractBidServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int userId = IntegerParser.parse(request.getParameter("userId"));
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        UserManage userManage = serviceSession.getService(UserManage.class);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        T6110_F06 userType = userManage.getUserType(userId);
        T6230 t6230 = bidManage.getBidType(loanId);
        //T6234[] t6234s = bidManage.searchBidDyw(loanId);
        T6235 t6235 = bidManage.finBidDywDate(loanId);
        request.setAttribute("t6230", t6230);
        request.setAttribute("userType", userType);
        request.setAttribute("t6235", t6235);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int userId = IntegerParser.parse(request.getParameter("userId"));
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        BidManage bidManage = serviceSession.getService(BidManage.class);
        T6230 t6230 = bidManage.getBidType(loanId);
        if (t6230 != null && t6230.F20 != T6230_F20.SQZ)
        {
            prompt(request, response, PromptLevel.ERROR, "不是申请中状态");
            sendRedirect(request, response, getController().getURI(request, LoanList.class));
            return;
        }
        T6235 t6235 = new T6235();
        t6235.F04 = request.getParameter("dywxx");
        t6235.F05 = loanId;
        bidManage.updateBidDywDate(t6235);
        if (t6230.F11 == T6230_F11.S)
        {
            sendRedirect(request, response, getController().getURI(request, AddGuaranteeXq.class) + "?loanId=" + loanId
                + "&userId=" + userId);
        }
        else
        {
            sendRedirect(request, response, getController().getURI(request, AddAnnexMsk.class) + "?loanId=" + loanId
                + "&userId=" + userId);
        }
    }
}
