package com.dimeng.p2p.console.servlets.info.yygl.spsc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.modules.base.console.service.AdvertisementManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_INFO_YYGL_MENU", name = "运营管理",moduleId="P2P_C_INFO_YYGL",order=0)
public class IndexSpsc extends AbstractSpscServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processGet(request, response, serviceSession);
	}

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		AdvertisementManage manage = serviceSession
	 			.getService(AdvertisementManage.class);
		manage.IndexSpsc(IntegerParser.parse(request.getParameter("id")));
		sendRedirect(request, response,
				getController().getURI(request, SearchSpsc.class));
	}

}
