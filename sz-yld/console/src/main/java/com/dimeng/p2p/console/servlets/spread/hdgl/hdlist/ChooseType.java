package com.dimeng.p2p.console.servlets.spread.hdgl.hdlist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;

/**
 * 
 * @author heluzhu
 *
 */
public class ChooseType extends AbstractSpreadServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request,response,serviceSession);
	}
	
	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		request.setAttribute("jlType", request.getParameter("jlType"));
		request.setAttribute("hdType", request.getParameter("hdType"));
		super.processPost(request, response, serviceSession);
	}
}
