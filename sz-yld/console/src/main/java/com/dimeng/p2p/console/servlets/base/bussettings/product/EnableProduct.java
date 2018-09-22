package com.dimeng.p2p.console.servlets.base.bussettings.product;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.ProductManage;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Right(id = "P2P_C_BASE_ENABLEPRODUCT", name = "启用产品", moduleId = "P2P_C_BASE_OPTSETTINGS_PRODUCT")
public class EnableProduct extends AbstractBaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ProductManage productManage = serviceSession.getService(ProductManage.class);
		int id = IntegerParser.parse(request.getParameter("id"));
		try {
			productManage.enable(id);
			sendRedirect(request, response,
					getController().getURI(request, SearchProduct.class));
		} catch (LogicalException | ParameterException e) {
			prompt(request, response, PromptLevel.WARRING, e.getMessage());
			processGet(request, response, serviceSession);
		}
	}
}
