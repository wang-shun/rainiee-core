package com.dimeng.p2p.console.servlets.account.vipmanage.grxx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_SHJLLIST", name = "审核记录",moduleId ="P2P_C_ACCOUNT_GRXX",order=4)
public class ShjlList extends AbstractAccountServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		GrManage manage = serviceSession.getService(GrManage.class);
		int id = IntegerParser.parse(request.getParameter("id"));
		int yhId = IntegerParser.parse(request.getParameter("yhId"));
		request.setAttribute("info", manage.rzjl(id,yhId)); 
		forwardView(request, response, getClass());
	}



}
