package com.dimeng.p2p.front.servlets.credit;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S62.entities.T6282;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.modules.bid.front.service.BidWillManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class Dkyx extends AbstractCreditServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		if(!FormToken.verify(serviceSession.getSession(), 
				request.getParameter(FormToken.parameterName()))) {
			throw new LogicalException("请不要重复提交请求！");
		}
		VerifyCodeAuthentication authentication = new VerifyCodeAuthentication();
		String verifyCode = request.getParameter("verifyCode");
		authentication.setVerifyCodeType("PS_LOAN_INTENT");
		authentication.setVerifyCode(verifyCode);
		ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
		String verifySfxyyzm = configureProvider.getProperty(SystemVariable.SFXYYZM);
		//是否需要输入验证码:false不需要
		if(!"false".equalsIgnoreCase(verifySfxyyzm))
		{
			serviceSession.getSession().authenticateVerifyCode(authentication);
		}

		BidWillManage manage = serviceSession.getService(BidWillManage.class);
		T6282 t6282 = new T6282();
		t6282.parse(request);
		t6282.F08 = IntegerParser.parse(request.getParameter("xian"));
		manage.add(t6282);
		getController().prompt(request, response, PromptLevel.INFO,
				"提交成功！我们将尽快和您联系");
		sendRedirect(request, response,
				getController().getViewURI(request, Dkyx.class));
	}
	
	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		String message = "系统繁忙，请稍后再试...";
		if(throwable != null && !StringHelper.isEmpty(throwable.getMessage())) {
			message = throwable.getMessage();
		}
		getController().prompt(request, response, PromptLevel.ERROR, message);
		//sendRedirect(request, response, getController().getViewURI(request, Dkyx.class));
		forward(request, response, getController().getViewURI(request, Dkyx.class));
	}
}
