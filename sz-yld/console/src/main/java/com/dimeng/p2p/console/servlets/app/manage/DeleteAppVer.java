package com.dimeng.p2p.console.servlets.app.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.AppVersionManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_APP_BBGL_DELETE", name = "删除", moduleId = "P2P_C_APP_BBGL", order = 2)
public class DeleteAppVer extends AbstractInformationServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        AppVersionManage manage = serviceSession.getService(AppVersionManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        if (id <= 0)
        {
            throw new ParameterException("参数错误！");
        }
        manage.delAppVersion(id);
        sendRedirect(request, response, getController().getURI(request, AppVerSearch.class));
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
