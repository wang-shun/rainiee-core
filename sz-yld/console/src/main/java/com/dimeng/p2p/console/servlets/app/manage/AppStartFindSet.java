package com.dimeng.p2p.console.servlets.app.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.AppStartFindSetManage;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord;

@Right(id = "P2P_C_APP_BBGL_STARFINDSET", isMenu = true, name = "启动发现页设置", moduleId = "P2P_C_APP_BBGL", order = 5)
public class AppStartFindSet extends AbstractInformationServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        AppStartFindSetManage appStartFindSetManage = serviceSession.getService(AppStartFindSetManage.class);
        AdvertisementRecord[] result = appStartFindSetManage.search();
        request.setAttribute("result", result);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
