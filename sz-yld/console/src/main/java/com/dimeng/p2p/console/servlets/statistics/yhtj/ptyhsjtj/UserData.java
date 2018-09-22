package com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.UserDataManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Right(id = "P2P_C_STATISTICS_USERDATA", name = "平台用户数据统计", moduleId = "P2P_C_STATISTICS_YHTJ_YHSJTJ")
public class UserData extends AbstractStatisticsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
							  HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		UserDataManage manage = serviceSession.getService(UserDataManage.class);
		request.setAttribute("userData", manage.getUserData());
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = calendar.getTime();
		Date endDate = new Date();
		Map<String, Object> result = manage.getRegisterUserData("day", DateParser.format(startDate), DateParser.format(endDate));
		request.setAttribute("result", result);
		forwardView(request, response, getClass());
	}

	@Override
	protected void processPost(HttpServletRequest request,
							   HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		try {
			UserDataManage manage = serviceSession.getService(UserDataManage.class);
			request.setAttribute("userData", manage.getUserData());
			String type = request.getParameter("type");
			Map<String, Object> result = manage.getRegisterUserData(StringHelper.isEmpty(type) ? "day" : type, request.getParameter("startDate"), request.getParameter("endDate"));
			request.setAttribute("result", result);
			forwardView(request, response, getClass());
		}catch (Throwable throwable){
			if (throwable instanceof ParameterException || throwable instanceof LogicalException) {
				prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
				processGet(request,response,serviceSession);
			} else {
				prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
				processGet(request, response, serviceSession);
			}
		}
	}

}
