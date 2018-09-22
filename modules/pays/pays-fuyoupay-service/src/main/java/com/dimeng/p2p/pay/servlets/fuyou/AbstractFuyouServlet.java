package com.dimeng.p2p.pay.servlets.fuyou;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.variables.defines.URLVariable;

public abstract class AbstractFuyouServlet extends AbstractServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		ResourceProvider resourceProvider = getResourceProvider();
		if (throwable instanceof AuthenticationException) {
			Controller controller = getController();
			controller.redirectLogin(request, response,
					resourceProvider.getResource(ConfigureProvider.class)
							.format(URLVariable.LOGIN));
		} else {
			resourceProvider.log(throwable);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected ConfigureProvider getConfigureProvider() {
		return getResourceProvider().getResource(ConfigureProvider.class);
	}
	

	protected String createSendForm(Map<String,String> param, String url) throws Throwable {
		StringBuilder builder = new StringBuilder();
		builder.append("<form action=\"");
		builder.append(url);
		
		builder.append("\" method=\"post\">");
		for(String key : param.keySet()){
			builder.append("<input type=\"hidden\" name=\""+key+"\" value=\"");
			builder.append(param.get(key));
			builder.append("\" />");
		}
		builder.append("</form>");
		builder.append("<script type=\"text/javascript\">");
		builder.append("document.forms[0].submit();");
		builder.append("</script>");
		
		logger.info("充值FORM表单请求：".concat(builder.toString()));
		return builder.toString();
	}
	
}
