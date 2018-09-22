package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.AnnexManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class SetPic extends AbstractBidServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		int userId = IntegerParser.parse(request.getParameter("userId"));
		int loanId = IntegerParser.parse(request.getParameter("loanId"));
		int id = IntegerParser.parse(request.getParameter("id"));
		AnnexManage annexManage = serviceSession.getService(AnnexManage.class);
		BidManage bidManage = serviceSession.getService(BidManage.class);
		final T6233 t6233 = annexManage.getFgk(id);
		String fileCode = t6233.F06;
		if (!StringHelper.isEmpty(fileCode)) {
			bidManage.setPic(loanId, fileCode,id);
		}
		getController().prompt(request, response, PromptLevel.WARRING, "设置成功");
		sendRedirect(request, response,
				getController().getURI(request, AddAnnexWz.class) + "?loanId="
						+ loanId + "&userId=" + userId);
	}
}
