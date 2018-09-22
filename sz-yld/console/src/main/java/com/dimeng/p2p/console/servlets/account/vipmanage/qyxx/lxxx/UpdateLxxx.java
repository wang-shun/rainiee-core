package com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6164;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList;
import com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ListCcxx;
import com.dimeng.p2p.modules.account.console.service.QyManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_QYXX_UPDATE", name = "修改",moduleId="P2P_C_ACCOUNT_QYXX",order=3)
public class UpdateLxxx extends AbstractAccountServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		QyManage manage = serviceSession.getService(QyManage.class);
		int id = IntegerParser.parse(request.getParameter("id"));
		request.setAttribute("info", manage.getLxxx(id));
		request.setAttribute("email", manage.getEmail(id));
		forwardView(request, response, getClass());
	}

	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		QyManage manage = serviceSession.getService(QyManage.class);
		int id = IntegerParser.parse(request.getParameter("id"));
		String email = request.getParameter("email");
		T6164 entity = new T6164();
		entity.parse(request);
		entity.F02 = IntegerParser.parse(request.getParameter("xian"));
		manage.updateLxxx(entity, email);
		if("submit".equals(request.getParameter("entryType"))){
            sendRedirect(request, response, getController().getURI(request, QyList.class));
            return;
        }
		sendRedirect(request, response, getController().getURI(request, ListCcxx.class) + "?id=" + id);
	}

	@Override
	protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
		forwardView(request, response, getClass());
	}
}
