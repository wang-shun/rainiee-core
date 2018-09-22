package com.dimeng.p2p.console.servlets.finance.zjgl.xxczgl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.OfflineChargeManage;
import com.dimeng.p2p.order.OfflineChargeOrderExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_FINANCE_XXCZSH", name = "审核", moduleId="P2P_C_FINANCE_ZJGL_XXCZGL", order = 2)
public class Xxczshtg extends AbstractFinanceServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
		OfflineChargeManage chargeManage = serviceSession
				.getService(OfflineChargeManage.class);
		int orderId = chargeManage.checkCharge(IntegerParser.parse(request.getParameter("id")),
				true);
		ResourceProvider resourceProvider = getResourceProvider();
		ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
		OfflineChargeOrderExecutor executor = resourceProvider
            .getResource(OfflineChargeOrderExecutor.class);
 		executor.submit(orderId, null);
		if(!tg)
		{
		    executor.confirm(orderId, null);
		}
		sendRedirect(request, response,
				getController().getURI(request, XxczglList.class));
	}

	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		if (throwable instanceof LogicalException
				|| throwable instanceof ParameterException) {
			getController().prompt(request, response, PromptLevel.WARRING,
					throwable.getMessage());
			sendRedirect(request, response,
					getController().getURI(request, XxczglList.class));
		} else {
			super.onThrowable(request, response, throwable);
		}
	}
}
