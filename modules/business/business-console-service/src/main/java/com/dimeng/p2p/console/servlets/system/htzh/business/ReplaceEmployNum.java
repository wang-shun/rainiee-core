package com.dimeng.p2p.console.servlets.system.htzh.business;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.modules.account.console.service.ZhglManage;

/**
 * 
 * 批量转出业务员
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月1日]
 */
@Right(id = "P2P_C_SYS_HTZH_YWYGL_REPLACEEMP", name = "客户转出", moduleId = "P2P_C_SYS_HTZH_YWYGL", order = 3)
public class ReplaceEmployNum extends AbstractBuisnessServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        String employeeNumOld = request.getParameter("employNumOld");
        String employeeNumNew = request.getParameter("employNumNew");
        ZhglManage manage = serviceSession.getService(ZhglManage.class);
        int inta = manage.replaceEmployNum(employeeNumNew, employeeNumOld);
        
        if (inta > 0)
        {
            getController().prompt(request, response, PromptLevel.INFO, "客户转出成功");
        }
        else
        {
            getController().prompt(request, response, PromptLevel.INFO, "客户转出失败");
        }
        sendRedirect(request, response, getController().getURI(request, BusinessUserList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        if (throwable instanceof SQLException || throwable instanceof ParameterException
            || throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
            throwable.getStackTrace();
            forwardController(request, response, BusinessUserList.class);
        }
        else
        {
            throwable.getStackTrace();
            super.onThrowable(request, response, throwable);
        }
    }
    
}
