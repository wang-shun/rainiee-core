package com.dimeng.p2p.console.servlets.base.jjr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S51.entities.T5128;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.JjrManage;

//@Right(id = "P2P_C_BASE_RANK_ADDJJR", name = "新增节假日")
public class AddJjr extends AbstractBaseServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        JjrManage manage = serviceSession.getService(JjrManage.class);
        T5128 entity = new T5128();
        entity.parse(request);
        manage.add(entity);
        sendRedirect(request, response, getController().getURI(request, JjrList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        if (throwable instanceof AuthenticationException)
        {
            Controller controller = getController();
            controller.redirectLogin(request, response, controller.getURI(request, AddJjr.class));
        }
        else
        {
            forwardView(request, response, getClass());
            //            sendRedirect(request, response,
            //                    getController().getURI(request, AddJjr.class) + "?F02=" + request.getParameter("F02"));
        }
    }
    
}
