package com.dimeng.p2p.console.servlets.base.optsettings.ptlogo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S71.entities.T7101;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.PtglManage;
import com.dimeng.p2p.modules.account.console.service.entity.Ptgl;
import com.dimeng.util.parser.IntegerParser;
import org.apache.log4j.Logger;

@MultipartConfig
@Right(id = "P2P_C_BASE_UPDATEPTLOGO", name = "修改平台图标管理", moduleId = "P2P_C_BASE_OPTSETTINGS_PTLOGO")
public class ViewPtgl extends AbstractAccountServlet {
	private static final long serialVersionUID = 1L;

	protected static final Logger logger = Logger.getLogger(ViewPtgl.class);

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		PtglManage manage = serviceSession.getService(PtglManage.class);
		int id = IntegerParser.parse(request.getParameter("id"));
		int index = IntegerParser.parse(request.getParameter("index"));
		T7101 entity = manage.get(id);
		if (entity == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		request.setAttribute("entity", entity);
		request.setAttribute("index", index);
		forwardView(request, response, getClass());
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
		PtglManage manage = serviceSession.getService(PtglManage.class);
		
		Ptgl ptgl = new Ptgl();
		ptgl.parse(request);
		manage.update(ptgl);
		getController().prompt(request, response, PromptLevel.INFO, "保存成功！");
		sendRedirect(request, response, getController().getURI(request, ViewPtgl.class)+"?id="+ptgl.F01+"&index=" + ptgl.index);
	}
	
	@Override
    protected void onThrowable(HttpServletRequest request,
            HttpServletResponse response, Throwable throwable)
            throws ServletException, IOException {
	    
	    int id = IntegerParser.parse(request.getParameter("F01"));
        int index = IntegerParser.parse(request.getParameter("index"));
        logger.error(throwable.getMessage());
        if (throwable instanceof SQLException) {
            getController().prompt(request, response, PromptLevel.ERROR,
                    "系统繁忙，请您稍后再试");
        } else if (throwable instanceof LogicalException
                || throwable instanceof ParameterException) {
            getController().prompt(request, response, PromptLevel.WARRING,
                    throwable.getMessage());
        } else {
            super.onThrowable(request, response, throwable);
        }
        sendRedirect(request, response, getController().getURI(request, ViewPtgl.class)+"?id="+id+"&index=" + index);
    }

}
