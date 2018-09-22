package com.dimeng.p2p.user.servlets.account.ccxx;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.account.user.service.UserBaseManage;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.user.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.user.servlets.account.QyBases;

public class AddQyCcxx extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            throw new LogicalException("请不要重复提交请求！");
        }
        UserBaseManage manage = serviceSession.getService(UserBaseManage.class);
        T6113 entity = new T6113();
        entity.parse(request);
        manage.addCcxx(entity);
        sendRedirect(request, response, getController().getViewURI(request, QyBases.class) + "?qyBasesFlag=5");
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, getController().getURI(request, AddQyCcxx.class));
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getURI(request, AddQyCcxx.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
