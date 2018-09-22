/*
 * 文 件 名:  ViewProgres.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修改时间:  2016年3月10日
 */
package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6248;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.Formater;
import com.dimeng.util.parser.IntegerParser;

/**
 * <标动态管理列表>
 * <功能详细描述>
 * 
 */
@Right(id = "P2P_C_LOAN_VIEW_BIDPROGRES", name = "动态管理", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 10)
public class ViewBidProgres extends AbstractBidServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1015689522L;
    
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
        UserManage userManage = serviceSession.getService(UserManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        T6230 loan = bidManage.get(loanId);
        if (!(loan.F20 == T6230_F20.HKZ || loan.F20 == T6230_F20.YJQ || loan.F20 == T6230_F20.YDF || loan.F20 == T6230_F20.DFZ))
        {
            throw new LogicalException("不是还款中、已结清、已垫付、垫付中状态");
        }
        String loanUser = userManage.getUserNameById(loan.F02);
        Map<String, String> infoMap = new HashMap<String, String>();
        infoMap.put("loanNumber", loan.F25);
        infoMap.put("loanTitle", loan.F03);
        infoMap.put("loanUser", loanUser);
        infoMap.put("loanAmount", Formater.formatAmount(loan.F05));
        infoMap.put("loanId", loanId + "");
        try
        {
            PagingResult<T6248> t6248List = bidManage.viewBidProgresList(loanId, new Paging()
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
            request.setAttribute("infoMap", infoMap);
            request.setAttribute("t6248List", t6248List);
            forwardView(request, response, getClass());
        }
        catch (Throwable throwable)
        {
            logger.error(throwable, throwable);
            if (throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                forwardView(request, response, getClass());
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
