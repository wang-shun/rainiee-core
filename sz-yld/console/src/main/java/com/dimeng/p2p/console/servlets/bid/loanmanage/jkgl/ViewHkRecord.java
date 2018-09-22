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
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.modules.bid.console.service.entity.Hkjllb;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_LOAN_VIEWPROJECT", name = "查看",moduleId="P2P_C_BID_JKGL_LOANMANAGE",order=2)
public class ViewHkRecord extends AbstractBidServlet {

	private static final long serialVersionUID = -268955794485557959L;

	@Override
    protected void processGet(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		int userId = IntegerParser.parse(request.getParameter("userId"));
		int loanId = IntegerParser.parse(request.getParameter("loanId"));
		UserManage userManage = serviceSession.getService(UserManage.class);
		BidManage bidManage = serviceSession.getService(BidManage.class);
		T6110_F06 userType = userManage.getUserType(userId);
		T6230 t6230 = bidManage.getBidType(loanId);
        PagingResult<Hkjllb> hkjllbs = bidManage.getHk(loanId, new Paging()
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
		request.setAttribute("t6230", t6230);
		request.setAttribute("userType", userType);
		request.setAttribute("hkjllbs", hkjllbs);
		forwardView(request, response, getClass());
	}
}
