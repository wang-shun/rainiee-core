package com.dimeng.p2p.console.servlets.base.optsettings.policy;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6149;
import com.dimeng.p2p.S61.enums.T6149_F07;
import com.dimeng.p2p.console.servlets.AbstractRankServlet;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;
import com.dimeng.util.StringHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Right(id = "P2P_C_BASE_POLICY_UPDATESTATUS", name = "修改评估问题状态", moduleId = "P2P_C_BASE_OPTSETTINGS_POLICY")
public class UpdateQuesStatus extends AbstractRankServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request,response,serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		try {
		    
		    String F01 = request.getParameter("F01");
			if(StringHelper.isEmpty(F01)){
				throw new ParameterException("修改的对象不存在");
			}
			RiskQuesManage manage = serviceSession.getService(RiskQuesManage.class);
			String status = request.getParameter("status");
			T6149 t6149 = manage.queryById(Integer.parseInt(F01));
			if(null == t6149){
				throw new ParameterException("修改的对象不存在");
			}
			String oldStatus = t6149.F07.name();
			t6149.F07 = !StringHelper.isEmpty(status) ? T6149_F07.parse(status) : T6149_F07.QY;
            manage.updateRiskQues(t6149, oldStatus);
			sendRedirect(request, response,
					getController().getURI(request, RiskQuesList.class));
		} catch (Throwable e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			logger.error(e, e);
			forwardController(request,response,RiskQuesList.class);
		}
	}

}
