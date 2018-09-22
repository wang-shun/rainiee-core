package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_LOAN_CHECK", name = "审核",moduleId="P2P_C_BID_JKGL_LOANMANAGE",order=4)
public class AdjustCredit extends AbstractBidServlet {

	private static final long serialVersionUID = -268955794485557959L;

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
		BigDecimal creditAmount = BigDecimalParser.parse(request
				.getParameter("creditAmount"));
		UserManage userManage = serviceSession.getService(UserManage.class);
		userManage.updateUserCredit(userId, creditAmount);
		sendRedirect(request, response,
				getController().getURI(request, DetailAnnexWz.class)
						+ "?loanId=" + loanId + "&userId=" + userId);
	}
}
