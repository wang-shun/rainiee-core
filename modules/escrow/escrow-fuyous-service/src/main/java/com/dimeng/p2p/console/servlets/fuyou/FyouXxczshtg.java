package com.dimeng.p2p.console.servlets.fuyou;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.service.OfflineChargeManage;
import com.dimeng.p2p.variables.defines.URLVariable;

/**
 * 
 * 审核通过
 * 
 * @author  heshiping
 * @version  [版本号, 2016年3月3日]
 */
@Right(id = "P2P_C_FINANCE_XXCZSH", name = "审核通过", moduleId = "P2P_C_FINANCE_ZJGL_XXCZGL", order = 2)
public class FyouXxczshtg extends AbstractFuyouServlet
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
        logger.info("线下充值审核通过——IP:" + request.getRemoteAddr());
        OfflineChargeManage chargeManage = serviceSession.getService(OfflineChargeManage.class);
        final int id = Integer.valueOf(request.getParameter("id"));
        if (id < 0)
        {
            throw new LogicalException("充值订单不存在!");
        }
        int orderId = chargeManage.checkCharge(id, true);
        if (orderId < 0)
        {
            throw new LogicalException("线下充值审核失败!");
        }
        chargeManage.exectCheck(serviceSession, orderId);
        sendRedirect(request, response, getConfigureProvider().format(URLVariable.XXCZGLLIST_URL));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getConfigureProvider().format(URLVariable.XXCZGLLIST_URL));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
