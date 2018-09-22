/*
 * 文 件 名:  BlzqDshCheck.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.console.servlets.finance.zjgl.blzq;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6264;
import com.dimeng.p2p.S62.enums.T6264_F04;
import com.dimeng.p2p.console.servlets.AbstractBadClaimServlet;
import com.dimeng.p2p.repeater.claim.BadClaimTransferManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * <不良债权审核>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月16日]
 */
@Right(id = "P2P_C_FINANCE_BLZQCHECK", name = "审核", moduleId = "P2P_C_FINANCE_BLZQZRGL", order = 2)
public class BlzqCheck extends AbstractBadClaimServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 461108202964641902L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        forwardView(request, response, BlzqCheck.class);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        BadClaimTransferManage badClaimTransferManage = serviceSession.getService(BadClaimTransferManage.class);
        T6264 t6264 = new T6264();
        t6264.F01 = IntegerParser.parse(request.getParameter("id"));
        t6264.F04 = T6264_F04.parse(request.getParameter("T6264_F04"));
        t6264.F05 = IntegerParser.parse(request.getParameter("lateDays"));
        t6264.F09 = new BigDecimal(request.getParameter("claimAmount"));
        t6264.F10 = new BigDecimal(request.getParameter("transferAmount"));
        t6264.F11 = request.getParameter("describe");
        badClaimTransferManage.check(t6264);
        if (t6264.F04 == T6264_F04.ZRZ)
        {
            sendRedirect(request, response, getController().getURI(request, BlzqZrzList.class));
            return;
        }
        sendRedirect(request, response, getController().getURI(request, BlzqZrsbList.class));
        
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
            getController().getURI(request, BlzqCheck.class) + "?id=" + request.getParameter("id"));
    }
}
