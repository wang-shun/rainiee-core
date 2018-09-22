package com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.AbstractZjdjglServlet;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.FYT6101;
import com.dimeng.p2p.escrow.fuyou.service.freeze.FreezeManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_FUYOU_ZJDJGL_ZJDJ_USERFUND_LIST", isMenu = true, name = "用户资金列表查看", moduleId = "P2P_C_FUYOU_ZJDJGL_ZJDJ", order = 1)
public class FreezeView extends AbstractZjdjglServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        
        String name = request.getParameter("name");//账户名
        if (StringHelper.isEmpty(name))
        {
            name = (String)request.getAttribute("name");
        }
        FreezeManage freezeManage = serviceSession.getService(FreezeManage.class);
        PagingResult<FYT6101> list = freezeManage.getT6101(name, new Paging()
        {
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        request.setAttribute("list", list);
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
