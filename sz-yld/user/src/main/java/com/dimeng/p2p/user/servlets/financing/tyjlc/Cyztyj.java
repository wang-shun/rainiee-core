package com.dimeng.p2p.user.servlets.financing.tyjlc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
@PagingServlet(itemServlet = Cyztyj.class)
public class Cyztyj extends AbstractFinancingServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -784201583820142095L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		
	}

}
