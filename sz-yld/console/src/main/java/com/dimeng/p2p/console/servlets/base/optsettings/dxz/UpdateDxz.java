package com.dimeng.p2p.console.servlets.base.optsettings.dxz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6217;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.bid.console.service.DxzBidManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BASE_DXZUPDATE", name = "修改", moduleId = "P2P_C_BASE_OPTSETTINGS_DXZTYPE", order = 3)
public class UpdateDxz extends AbstractSystemServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		int id = IntegerParser.parse(request.getParameter("id"));
        DxzBidManage dxzBidManage = serviceSession.getService(DxzBidManage.class);
        T6217 t6217 = dxzBidManage.getT6217(id);
        request.setAttribute("t6217", t6217);
		forwardView(request, response, getClass());
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		try{
            DxzBidManage dxzBidManage = serviceSession.getService(DxzBidManage.class);
            T6217 t6217 = new T6217();
            t6217.parse(request);
            dxzBidManage.updateT6217(t6217);
            sendRedirect(request, response, getController().getURI(request, DxzList.class));
		} catch (Throwable e) {
			if (e instanceof ParameterException || e instanceof LogicalException) {
				getController().prompt(request, response, PromptLevel.WARRING,e.getMessage());
				processGet(request, response, serviceSession);
			} else {
				super.onThrowable(request, response, e);
			}
		}
	}
}
