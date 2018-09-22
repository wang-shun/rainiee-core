package com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.QyManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_UPDATEJGXX", name = "修改",moduleId="P2P_C_ACCOUNT_JGXX",order=2)
public class AddFcxx extends AbstractAccountServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		forwardView(request, response, getClass());
	}

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		QyManage manage = serviceSession.getService(QyManage.class);
		T6112 entity = new T6112();
		entity.parse(request);
		entity.F08 = IntegerParser.parse(request.getParameter("xian"));
		manage.addFcxx(entity);
		int id = IntegerParser.parse(request.getParameter("id"));
		sendRedirect(request, response, getController().getURI(request, ListFcxx.class)+"?id="+id);
	}

	
}
