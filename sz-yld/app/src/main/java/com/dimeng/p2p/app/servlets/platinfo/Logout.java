package com.dimeng.p2p.app.servlets.platinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;

/**
 * 
 * 登录退出
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月18日]
 */
public class Logout extends AbstractAppServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        Session session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        if (session != null)
        {
            session.invalidate(request, response);
            session.setAttribute("fromLogout","true");
        }
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success");
        return;
    }
}
