package com.dimeng.p2p.console.servlets.account;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.QyManage;

public class Check extends AbstractAccountServlet{
	private static final long serialVersionUID = 1L;
	/**
	 * 检查手机号码是否已经存在
	 */
	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		QyManage qyManage = serviceSession.getService(QyManage.class);
		PrintWriter out=response.getWriter();
		String phone = request.getParameter("phone");
		int id = Integer.parseInt(request.getParameter("id"));
		out.print(qyManage.isPhoneExist(phone,id));
	}
	
}
