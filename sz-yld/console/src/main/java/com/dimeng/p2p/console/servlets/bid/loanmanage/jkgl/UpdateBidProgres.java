/*
 * 文 件 名:  UpdateProgres.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修改时间:  2015年3月10日
 */
package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6248;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * <修改进展信息>
 * <功能详细描述>
 * 
 */
@Right(id = "P2P_C_LOAN_VIEW_BIDPROGRES", name = "动态管理", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 10)
public class UpdateBidProgres extends AbstractBidServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 512363221L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int pId = IntegerParser.parse(request.getParameter("pId"));
        
        BidManage bidManage = serviceSession.getService(BidManage.class);
        T6248 t6248 = bidManage.viewBidProgres(pId);
        request.setAttribute("t6248", t6248);
        request.setAttribute("pId", pId);
        request.setAttribute("loanId", loanId);
        super.processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int pId = IntegerParser.parse(request.getParameter("pId"));
        try
        {
            BidManage bidManage = serviceSession.getService(BidManage.class);
            T6248 t6248 = new T6248();
            t6248.parse(request);
            t6248.F01 = pId;
            t6248.F03 = loanId;
            
            if (StringHelper.isEmpty(t6248.F04) || StringHelper.isEmpty(t6248.F06) || null == t6248.F08)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "主题标题,标题时间,简要介绍不能为空！");
                // 跳转到信息页面
                forwardView(request, response, getClass());
                return;
            }
            bidManage.updateBidProgres(t6248);
            // 跳转到倡议书信息页面
            sendRedirect(request, response, getController().getURI(request, ViewBidProgres.class) + "?loanId=" + loanId);
        }
        catch (Throwable throwable)
        {
            logger.error(throwable, throwable);
            if (throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                processGet(request, response, serviceSession);
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
