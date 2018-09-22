package com.dimeng.p2p.console.servlets.account.vipmanage.zhgl;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;

@Right(id = "P2P_C_ACCOUNT_RESET_LOGIN_ERROR", name = "登录错误次数重置",moduleId= "P2P_C_ACCOUNT_ZHGL",order=9)
public class ResetLoginError extends AbstractAccountServlet
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
            UserManage manage = serviceSession.getService(UserManage.class);
            String userName = request.getParameter("userName");
            manage.resetLoginErrorNum(userName);
            out.print("重置成功！");
        }
        catch (Throwable e)
        {
            out.print("重置失败！请联系系统管理员！");
        }
    }
    
}
