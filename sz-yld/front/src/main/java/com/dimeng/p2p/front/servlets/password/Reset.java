/*
 * 文 件 名:  Reset.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huanggang
 * 修改时间:  2015年10月8日
 */
package com.dimeng.p2p.front.servlets.password;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.front.service.PasswordManage;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
/**
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年12月4日]
 */
public class Reset extends AbstractPasswordServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response,ServiceSession serviceSession) throws Throwable {
		
		ConfigureProvider configureProvider = ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
		String password = StringHelper.isEmpty(request.getParameter("password")) ? "" : request.getParameter("password");
		String repassword = StringHelper.isEmpty(request.getParameter("repassword")) ? "" : request.getParameter("repassword");
		password = RSAUtils.decryptStringByJs(password);
		repassword = RSAUtils.decryptStringByJs(repassword);
        int id = IntegerParser.parse(serviceSession.getSession().getAttribute("PASSWORD_ACCOUNT_ID"));
        if (id <= 0)
        {
            sendRedirect(request, response, getController().getViewURI(request, Index.class));
            return;
        }
        serviceSession.getSession().setAttribute(id + "", "CHECK_SUCCESS");
		if (StringHelper.isEmpty(password)) {
			getController().prompt(request, response, PromptLevel.WARRING, "密码不能为空");
			sendRedirect(request, response, getController().getViewURI(request, Reset.class));
			return;
		}

		if (!password.equals(repassword)) {
			getController().prompt(request, response, PromptLevel.WARRING, "两次输入不符");
			sendRedirect(request, response, getController().getViewURI(request, Reset.class));
			return;
		}
		// 校验密码是否匹配正则规则
		String regEx = configureProvider.getProperty(SystemVariable.NEW_PASSWORD_REGEX).substring(
				1, configureProvider.getProperty(SystemVariable.NEW_PASSWORD_REGEX).length() - 1);
		if (!password.matches(regEx)) {
			getController().prompt(request, response, PromptLevel.WARRING,configureProvider.getProperty(SystemVariable.PASSWORD_REGEX_CONTENT));
			sendRedirect(request, response, getController().getViewURI(request, Reset.class));
			return;
		}

		PasswordManage passwordManage = serviceSession.getService(PasswordManage.class);
		passwordManage.updatePassword(password, id);
        serviceSession.getSession().setAttribute(id + "", "");
		sendRedirect(request, response, getController().getViewURI(request, Success.class));
	}

	@Override
	protected void onThrowable(HttpServletRequest request, HttpServletResponse response,
			Throwable throwable) throws ServletException, IOException {
		if (throwable instanceof AuthenticationException) {
			getController().prompt(request, response, PromptLevel.ERROR, "校验码已过期或输入校验码错误");
			sendRedirect(request, response, getController().getViewURI(request, Reset.class));
        }
        else
        {
			getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
			sendRedirect(request, response, getController().getViewURI(request, Reset.class));
		}
	}

}
