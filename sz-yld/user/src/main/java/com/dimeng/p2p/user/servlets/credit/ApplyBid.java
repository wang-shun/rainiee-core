package com.dimeng.p2p.user.servlets.credit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdxq;

/**
 * 借款申请查询
 *
 */
public class ApplyBid extends AbstractCreditServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		int bid = Integer.parseInt(request.getParameter("id"));
		BidManage bidManage = serviceSession.getService(BidManage.class);
		Bdxq bdxq = bidManage.getT6230(bid);
		if(bdxq.F20 == T6230_F20.YLB){
			/*prompt(request, response, PromptLevel.WARRING, "该标已流标");
			//sendRedirect(request, response, getController().getURI(request, Apply.class));
			forwardView(request, response, Apply.class);*/
			response.getWriter().print("YLB");
		}else{
			/*sendRedirect(request, response, request.getParameter("url"));*/
			//response.getWriter().print(nu);
		}
	}
}
