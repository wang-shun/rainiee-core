package com.dimeng.p2p.console.servlets.system.htzh.sys;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;

@Right(id ="P2P_C_SYS_HTGL_GLYGL_DLCWCSCZ", name = "登录错误次数重置",moduleId="P2P_C_SYS_HTZH_GLYGL",order=4)
public class ResetConsoleLoginError extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        try
        {
            SysUserManage manage = serviceSession.getService(SysUserManage.class);
            String userName = request.getParameter("userName");
            manage.resetConsoleLoginErrorNum(userName);
            out.print("重置成功！");
        }
        catch (Throwable e)
        {
            log(e.getMessage());
            out.print("重置失败！请联系系统管理员！");
        }
    }
    
}
