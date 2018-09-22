package com.dimeng.p2p.console.servlets.base.bussettings.ptdf;

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
import com.dimeng.p2p.S51.entities.T5131;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.DywlxManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BASE_PTDFMANAGER", name = "不良资产处理方案设置", isMenu = true, moduleId = "P2P_C_BASE_OPTSETTINGS_PTDF")
public class UpdatePtdfType extends AbstractBaseServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        DywlxManage dywlxManage = serviceSession.getService(DywlxManage.class);
        T5131 ptdfType = dywlxManage.getPtdfType();
        request.setAttribute("ptdfType", ptdfType);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        DywlxManage dywlxManage = serviceSession.getService(DywlxManage.class);
        dywlxManage.updateT5031(IntegerParser.parse(request.getParameter("id")),
            T5131_F02.parse(request.getParameter("dfType")));
        getController().prompt(request, response, PromptLevel.INFO, "不良资产处理方案设置成功");
        sendRedirect(request, response, getController().getURI(request, UpdatePtdfType.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof SQLException)
        {
            logger.error(throwable, throwable);
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, getController().getURI(request, UpdatePtdfType.class));
            
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getURI(request, UpdatePtdfType.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
            sendRedirect(request, response, getController().getURI(request, UpdatePtdfType.class));
        }
    }
    
}
