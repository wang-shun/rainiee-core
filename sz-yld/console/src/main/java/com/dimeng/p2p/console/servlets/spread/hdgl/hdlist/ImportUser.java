package com.dimeng.p2p.console.servlets.spread.hdgl.hdlist;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6340;
import com.dimeng.p2p.console.config.ConsoleConst;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.AddHdgl;
import com.dimeng.p2p.modules.activity.console.service.ActivityManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
@MultipartConfig
public class ImportUser extends AbstractSpreadServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ActivityManage activityManage = serviceSession.getService(ActivityManage.class);
		ResourceProvider resourceProvider = getResourceProvider();
		Part part = request.getPart("file");
		if (!StringHelper.isEmpty(part.getContentType())
				&& (part.getContentType().equals(ConsoleConst.CVS_STR)
						|| part.getContentType().equals(ConsoleConst.TXT_STR) 
						|| part.getContentType().equals(ConsoleConst.CVS_JS_STR))) {
			String[] users = activityManage.importUsers(part.getInputStream(),
					resourceProvider.getCharset());
			request.setAttribute("users", users);
		}else{
			logger.info("文件格式不正确，仅支持CSV、TXT格式");
		    throw new ParameterException("文件格式不正确，仅支持CSV、TXT格式");
		}
		String id = request.getParameter("id");
		if(StringHelper.isEmpty(id)){
    		request.setAttribute("hdType", request.getParameter("hdType"));
    		request.setAttribute("jlType", request.getParameter("jlType"));
		}else{
		    ActivityManage manage = serviceSession.getService(ActivityManage.class);
	        T6340 activity = manage.getActivity(IntegerParser.parse(id));
	        request.setAttribute("activity", activity);
		}
        /*forwardView(request, response, AddHdgl.class);*/
        forward(request, response, getController().getViewURI(request, AddHdgl.class));
	}

	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		//super.onThrowable(request, response, throwable);
	    prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
	    /*forwardView(request, response, AddHdgl.class);*/
        forward(request, response, getController().getViewURI(request, AddHdgl.class));
	}
}
