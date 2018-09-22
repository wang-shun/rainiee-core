package com.dimeng.p2p.console.servlets.base.optsettings.policy;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6149;
import com.dimeng.p2p.S61.enums.T6149_F07;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.AbstractRankServlet;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;
import com.dimeng.util.StringHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Right(id = "P2P_C_BASE_POLICY_ADD", name = "新增评估问题", moduleId = "P2P_C_BASE_OPTSETTINGS_POLICY")
public class AddRiskQues extends AbstractRankServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		forwardView(request, response, getClass());
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		try {
		    if(!FormToken.verify(serviceSession.getSession(),
                request.getParameter(FormToken.parameterName()))) {
		        throw new LogicalException("请不要重复提交请求！");
		    }
		    RiskQuesManage manage = serviceSession.getService(RiskQuesManage.class);
			String title = request.getParameter("F02");
			String oA = request.getParameter("F03");
			String oB = request.getParameter("F04");
			String oC = request.getParameter("F05");
			String oD = request.getParameter("F06");
			String status = request.getParameter("F07");
			String order = request.getParameter("F08");
			T6149 t6149 = new T6149();
			t6149.F02 = title;
			t6149.F03 = oA;
			t6149.F04 = oB;
			t6149.F05 = oC;
			t6149.F06 = oD;
			t6149.F07 = !StringHelper.isEmpty(status) ? T6149_F07.parse(status) : T6149_F07.QY;
			t6149.F08 = !StringHelper.isEmpty(order) ? Integer.parseInt(order) : 0;
			manage.addRiskQues(t6149);
			sendRedirect(request, response,
					getController().getURI(request, RiskQuesList.class));
		} catch (Throwable e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			logger.error(e, e);
			processGet(request, response, serviceSession);
		}
	}

}
