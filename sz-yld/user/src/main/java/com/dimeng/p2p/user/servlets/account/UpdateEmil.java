package com.dimeng.p2p.user.servlets.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.util.StringHelper;

public class UpdateEmil extends AbstractAccountServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		String emil = request.getParameter("bemil");
		String code = request.getParameter("bemilCode");
		SafetyManage safetyManage = serviceSession
				.getService(SafetyManage.class);
		if (!StringHelper.isEmpty(emil) && !StringHelper.isEmpty(code)) {
			String scode = serviceSession.getSession().getAttribute("code");
			if (!StringHelper.isEmpty(scode)) {
				if (scode.split(",").length >= 3) {
					String e = scode.split(",")[1];
					String c = scode.split(",")[2];
					if (emil.endsWith(e) && code.equals(c)) {
						safetyManage.updateEmil(emil);
					}
				}
			} else {
				getController().prompt(request, response, PromptLevel.ERROR,
						"验证码输入错误");
				forward(request, response,
						getController().getViewURI(request, Safetymsg.class));
			}
		}

		sendRedirect(request, response,
				getController().getViewURI(request, Safetymsg.class));
	}

}
