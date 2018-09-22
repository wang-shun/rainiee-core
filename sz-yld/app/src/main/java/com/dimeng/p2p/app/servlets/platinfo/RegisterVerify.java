package com.dimeng.p2p.app.servlets.platinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;

/**
 * 
 * 注册验证码
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月18日]
 */
public class RegisterVerify extends AbstractAppServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        Session session = serviceSession.getSession();
        if (session == null)
        {
            session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        }
        session.invalidVerifyCode("REGISTER");
        final String verifyCode = session.getVerifyCode("REGISTER");
        showKaptcha(verifyCode, response);
    }
}
