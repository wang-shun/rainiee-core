package com.dimeng.p2p.user.servlets.ret;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.service.ServiceSession;

public abstract class AbstractShuangQianServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	protected void printMark(HttpServletResponse response, String mark)
			throws Throwable {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.write("SUCCESS");
		writer.flush();
		writer.close();
	}

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected boolean mustAuthenticated() {
		return false;
	}
    
    protected ConfigureProvider getConfigureProvider()
    {
        return getResourceProvider().getResource(ConfigureProvider.class);
    }

}
