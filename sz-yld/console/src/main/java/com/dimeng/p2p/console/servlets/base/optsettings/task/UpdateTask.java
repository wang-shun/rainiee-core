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
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.service.TaskManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

@Right(id = "P2P_C_BASE_TASK_UPDATE", name = "修改任务", moduleId = "P2P_C_BASE_OPTSETTINGS_TASK")
public class UpdateTask extends AbstractBaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
	    String id = request.getParameter("id");
	    if(StringHelper.isEmpty(id)){
	        throw new LogicalException("此任务不存在");
	    }
	    TaskManage manage = serviceSession.getService(TaskManage.class);
	    T6601 t6601 = manage.queryById(Integer.parseInt(id));
	    request.setAttribute("t6601", t6601);
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
		    String id = request.getParameter("F01");
			String name = request.getParameter("F02");
			String className = request.getParameter("F03");
			String methodName = request.getParameter("F04");
			String invokeTime = request.getParameter("F05");
			String remark = request.getParameter("F11");
			T6601 t6601 = manage.queryById(Integer.parseInt(id));
			t6601.F02 = name;
			t6601.F03 = className;
			t6601.F04 = methodName;
			t6601.F05 = invokeTime;
			t6601.F11 = remark;
			logger.info("===========开始修改任务信息ID:"+id+"===========");
			manage.updateInfo(t6601);
			Map<String,String> param = new HashMap<String,String>();
			param.put("id", id);
			param.put("method", "update");
			final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
			String url = configureProvider.format(URLVariable.EDITJOB_URL);
			HttpClientUtil.doPost(url, param, "utf-8");
			logger.info("===========结束修改任务信息ID:"+id+"===========");
			sendRedirect(request, response,
					getController().getURI(request, TaskList.class));
		} catch (Throwable e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			logger.error(e, e);
			processGet(request, response, serviceSession);
		}
	}

}
