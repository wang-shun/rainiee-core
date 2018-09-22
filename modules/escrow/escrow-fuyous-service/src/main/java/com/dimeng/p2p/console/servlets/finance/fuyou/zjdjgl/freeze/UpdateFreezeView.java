package com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.AbstractZjdjglServlet;
import com.dimeng.p2p.escrow.fuyou.service.freeze.FreezeManage;
import com.dimeng.util.StringHelper;

/**
 * 
 * 冻结页面
 *
 */
@Right(id = "P2P_C_FINANCE_UPDATE_FREEZEVIEW", isMenu = true, name = "资金冻结操作", moduleId = "P2P_C_FUYOU_ZJDJGL_ZJDJ", order = 2)
public class UpdateFreezeView extends AbstractZjdjglServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        this.processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        if (request.getParameter("name") != null || request.getAttribute("name") != null)
        {
            String name = "";
            if (!StringHelper.isEmpty(request.getParameter("name")))
            {
                name = new String(request.getParameter("name").getBytes("iso8859-1"), "UTF-8");
            }
            else
            {
                name = (String)request.getAttribute("name");
            }
            FreezeManage freezeManage = serviceSession.getService(FreezeManage.class);
            T6101 t6101 = freezeManage.selectT6101(name, T6101_F03.WLZH);
            request.setAttribute("t6101", t6101);
        }
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        Controller controller = getController();
        controller.prompt(request, response, PromptLevel.INFO, throwable.getMessage());
        sendRedirect(request, response, controller.getViewURI(request, getClass()));
    }
    
}
