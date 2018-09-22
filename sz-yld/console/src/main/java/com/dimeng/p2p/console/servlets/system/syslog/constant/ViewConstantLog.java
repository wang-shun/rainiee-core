package com.dimeng.p2p.console.servlets.system.syslog.constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.ConstantManage;

public class ViewConstantLog extends AbstractSystemServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void processGet(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}
	
	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ConstantManage manage = serviceSession.getService(ConstantManage.class);
		request.setAttribute("result", manage.selectById(Integer.parseInt(request.getParameter("id"))));
		forwardView(request, response, getClass());
	}
}
