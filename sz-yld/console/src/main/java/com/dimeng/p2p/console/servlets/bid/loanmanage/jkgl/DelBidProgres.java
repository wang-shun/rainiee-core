/*
 * 文 件 名:  DelProgres.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 */
package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * <删除动态>
 * <功能详细描述>
 * 
 */
@Right(id = "P2P_C_LOAN_VIEW_BIDPROGRES", name = "动态管理", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 10)
public class DelBidProgres extends AbstractBidServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 123854961231L;
    
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
        
        BidManage bidManage = serviceSession.getService(BidManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int pId = IntegerParser.parse(request.getParameter("pId"));
        try
        {
            bidManage.deleteBidProgres(loanId, pId);
            sendRedirect(request, response, getController().getURI(request, ViewBidProgres.class) + "?loanId=" + loanId);
        }
        catch (Throwable throwable)
        {
            logger.error(throwable, throwable);
            if (throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, ViewBidProgres.class) + "?loanId="
                    + loanId);
            }
            else if (throwable instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                forwardView(request, response, LoanList.class);
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
    
}
