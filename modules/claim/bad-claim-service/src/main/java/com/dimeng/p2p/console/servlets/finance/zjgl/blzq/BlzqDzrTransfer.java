/*
 * 文 件 名:  BlzqDzrTransfer.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月15日
 */
package com.dimeng.p2p.console.servlets.finance.zjgl.blzq;

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
import com.dimeng.p2p.console.servlets.AbstractBadClaimServlet;
import com.dimeng.p2p.repeater.claim.BadClaimTransferManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * <不良债权转让>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月15日]
 */
@Right(id = "P2P_C_FINANCE_BLZQDZRTRANSFER", name = "转让", moduleId = "P2P_C_FINANCE_BLZQZRGL", order = 1)
public class BlzqDzrTransfer extends AbstractBadClaimServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8713679841427583312L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        forwardView(request, response, BlzqDzrTransfer.class);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        BadClaimTransferManage badClaimTransferManage = serviceSession.getService(BadClaimTransferManage.class);
        badClaimTransferManage.transfer(IntegerParser.parse(request.getParameter("loanId")),
            IntegerParser.parse(request.getParameter("periodId")));
        sendRedirect(request, response, getController().getURI(request, BlzqDshList.class));
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
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
        sendRedirect(request,
            response,
            getController().getURI(request, BlzqDzrTransfer.class) + "?loanId=" + request.getParameter("loanId"));
    }
    
}
