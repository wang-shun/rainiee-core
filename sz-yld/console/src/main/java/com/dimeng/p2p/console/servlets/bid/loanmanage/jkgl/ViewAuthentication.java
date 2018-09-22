package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.account.console.service.entity.Rzxx;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_LOAN_VIEWPROJECT", name = "查看",moduleId="P2P_C_BID_JKGL_LOANMANAGE",order=2)
public class ViewAuthentication extends AbstractBidServlet
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
        GrManage personalManage = serviceSession.getService(GrManage.class);
        UserManage userManage = serviceSession.getService(UserManage.class);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        int userId = IntegerParser.parse(request.getParameter("userId"));
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        T6110_F06 userType = userManage.getUserType(userId);
        PagingResult<Rzxx> authentications = personalManage.getRzxx(userId, new Paging()
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
        T6230 t6230 = bidManage.getBidType(loanId);
        request.setAttribute("userType", userType);
        request.setAttribute("t6230", t6230);
        request.setAttribute("authentications", authentications);
        forwardView(request, response, getClass());
    }
}
