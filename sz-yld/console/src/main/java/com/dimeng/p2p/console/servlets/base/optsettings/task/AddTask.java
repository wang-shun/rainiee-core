package com.dimeng.p2p.console.servlets.base.optsettings.task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S66.entities.T6601;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.config.util.HttpClientUtil;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.service.TaskManage;
import com.dimeng.p2p.variables.defines.URLVariable;

import java.util.HashMap;
import java.util.Map;

@Right(id = "P2P_C_BASE_TASK_ADD", name = "增加任务", moduleId = "P2P_C_BASE_OPTSETTINGS_TASK")
public class AddTask extends AbstractSpreadServlet {

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
		    TaskManage manage = serviceSession.getService(TaskManage.class);
			String name = request.getParameter("F02");
			String className = request.getParameter("F03");
			String methodName = request.getParameter("F04");
			String invokeTime = request.getParameter("F05");
			String remark = request.getParameter("F11");
			T6601 t6601 = new T6601();
			t6601.F02 = name;
			t6601.F03 = className;
			t6601.F04 = methodName;
			t6601.F05 = invokeTime;
			t6601.F11 = remark;
			logger.info("===========开始添加任务===========");
			int id = manage.insertoInfo(t6601);
			Map<String,String> param = new HashMap<String,String>();
			param.put("id", id + "");
			param.put("method", "add");
			final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
			String url = configureProvider.format(URLVariable.EDITJOB_URL);
			HttpClientUtil.doPost(url, param, "utf-8");
			logger.info("===========结束添加任务===========");
			sendRedirect(request, response,
					getController().getURI(request, TaskList.class));
		} catch (Throwable e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			e.printStackTrace();
			processGet(request, response, serviceSession);
		}
	}

}
