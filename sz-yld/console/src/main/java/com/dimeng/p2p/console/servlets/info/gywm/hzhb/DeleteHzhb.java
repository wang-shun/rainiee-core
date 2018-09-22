package com.dimeng.p2p.console.servlets.info.gywm.hzhb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.PartnerManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_INFO_GYWM_MENU", name = "关于我们",moduleId="P2P_C_INFO_GYWM",order=0)
public class DeleteHzhb extends AbstractInformationServlet {

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
		PartnerManage manage = serviceSession.getService(PartnerManage.class);
		manage.delete(IntegerParser.parseArray(request.getParameterValues("id")));
		sendRedirect(request, response,
				getController().getURI(request, SearchHzhb.class));
	}

}
