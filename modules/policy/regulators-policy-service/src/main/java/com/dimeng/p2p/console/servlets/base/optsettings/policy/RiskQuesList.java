package com.dimeng.p2p.console.servlets.base.optsettings.policy;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractRankServlet;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;
import com.dimeng.p2p.repeater.policy.query.QuesQuery;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Right(id = "P2P_C_BASE_RISK_LIST", isMenu = true, name = "风险评估问题设置", moduleId = "P2P_C_BASE_OPTSETTINGS_POLICY",order = 0)
public class RiskQuesList extends AbstractRankServlet
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
        
        RiskQuesManage manage = serviceSession.getService(RiskQuesManage.class);
        Paging paging = new Paging()
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
        };
        String tilte = request.getParameter("title");
        String status1 = request.getParameter("status1");
        PagingResult<QuesQuery> result = manage.queryAllQues(tilte, status1, paging);
        request.setAttribute("result", result);
        if ("0001".equals(request.getParameter("errorCode")))
        {
            request.setAttribute("errorInfo", "启用问题数已达20，不可启用");
        }
        forwardView(request, response, getClass());
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
            sendRedirect(request, response, getController().getURI(request, RiskQuesList.class));
            
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getURI(request, RiskQuesList.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
            sendRedirect(request, response, getController().getURI(request, RiskQuesList.class));
        }
    }
    
}
