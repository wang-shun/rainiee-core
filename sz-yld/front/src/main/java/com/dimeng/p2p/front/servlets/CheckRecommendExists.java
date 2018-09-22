package com.dimeng.p2p.front.servlets;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.account.front.service.UserManage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 校验推荐码是否正确
 */
public class CheckRecommendExists extends AbstractFrontServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean mustAuthenticated() {
		return false;
	}

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
		response.setContentType("text/html;charset="
				+ getResourceProvider().getCharset());
		String code = request.getParameter("code");
		UserManage userManager = serviceSession.getService(UserManage.class);
		int resultCode = userManager.checkCodeExist(code);
		response.getWriter().println(resultCode);
		response.getWriter().flush();
	}

	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		if (throwable instanceof SQLException
				|| throwable instanceof ParameterException
				|| throwable instanceof LogicalException) {
			getController().prompt(request, response, PromptLevel.ERROR,
					throwable.getMessage());
			throwable.getStackTrace();
			sendRedirect(request, response,
					getController().getViewURI(request, Register.class));
		} else {
			super.onThrowable(request, response, throwable);
		}
	}
}
